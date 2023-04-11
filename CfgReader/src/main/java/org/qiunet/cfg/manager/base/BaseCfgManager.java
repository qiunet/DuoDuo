package org.qiunet.cfg.manager.base;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.base.ICfgDelayLoadData;
import org.qiunet.cfg.event.CfgManagerAddEvent;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.manager.exception.UnknownFieldException;
import org.qiunet.utils.convert.ConvertManager;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.file.DPath;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.file.IFileChangeCallback;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author  zhengj
 * Date: 2019/6/6.
 * Time: 15:51.
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseCfgManager<ID, Cfg extends ICfg<ID>> implements ICfgManager<ID, Cfg> {
	protected Logger logger = LoggerType.DUODUO_CFG_READER.getLogger();
	/**
	 * 需要热加载的cfgManagers
	 */
	private static final Set<ICfgManager<?, ?>> needReloadCfgs = Sets.newConcurrentHashSet();
	/**
	 * 是否需要重新加载.
	 */
	private static final AtomicBoolean needReloadCfg = new AtomicBoolean(false);
	/**
	 * 需要延迟加载的字段
	 */
	final ArrayList<Field> delayLoadFields = new ArrayList<>();
	/**
	 * 文件名.
	 * 或者pattern
	 */
	protected final String fileName;
	/**
	 * 所有文件
	 */
	private final File [] files;
	/**
	 * cfg class name
	 */
	protected Class<Cfg> cfgClass;
	/**
	 * 加载顺序
	 */
	private final int order;
	/**
	 * 所有数据
	 */
	private List<Cfg> cfgList;

	protected BaseCfgManager(Class<Cfg> cfgClass) {
		org.qiunet.cfg.annotation.Cfg cfg = cfgClass.getAnnotation(org.qiunet.cfg.annotation.Cfg.class);
		this.fileName = cfg.value();
		this.cfgClass = cfgClass;
		this.order = cfg.order();
		this.files = getFiles();

		this.fileChangeListener();
		this.checkCfgClass();

		CfgManagerAddEvent.fireEvent(this);
	}

	@Override
	public void loadCfg() throws Exception {
		ICfgWrapper<ID, Cfg> wrapper = buildWrapper(readCfgList(files));
		LoadSandbox.instance.addWrapper(wrapper);
	}
	/**
	 * 读取cfg list
	 * @return list
	 */
	protected abstract List<Cfg> readCfgList(File [] files);
	/**
	 * 获取配置文件真实路径
	 * @return File数组
	 */
	private File [] getFiles() {
		if(fileName.contains("*")) {
			List<File> files = Lists.newLinkedList();
			String dirName = Objects.requireNonNull(getClass().getClassLoader().getResource(DPath.dirName(fileName))).getFile();
			String finalFileName = DPath.fileName(fileName).replaceAll("\\.", "\\\\.").replaceAll("\\*", "(.*)");
			DPath.listDir(dirName, files::add, file -> file.getName().matches(finalFileName));
			return files.toArray(new File[0]);
		}
		String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile();
		return new File[]{ new File(filePath)};
	}
	/**
	 * 初始化数据
	 * @param wrapper 包含数据的沙盒 wrapper
	 */
	void switchCfgToRuntime(ICfgWrapper<ID, Cfg> wrapper) {
		this.cfgList = wrapper.list();
		this.loadCfg0(wrapper);
	}

	/***
	 * 初始化数据
	 * @param wrapper 沙盒的wrapper
	 */
	protected abstract void loadCfg0(ICfgWrapper<ID, Cfg> wrapper);
	/**
	 * 构造一个 wrapper 给沙盒
	 * @param cfgList list
	 * @return wrapper
	 */
	protected abstract ICfgWrapper<ID, Cfg> buildWrapper(List<Cfg> cfgList);

	@Override
	public List<Cfg> list() {
		return cfgList;
	}
	/**
	 * 监听文件变动.
	 * 一个cfgManager 可能有多个文件. 延时500 毫秒再加载.
	 */
	protected void fileChangeListener() {
		if ( SystemPropertyUtil.getOsName().is(SystemPropertyUtil.OSType.LINUX)) {
			// linux 正式环境. 还是人来决定什么时候更新好点.
			return;
		}

		IFileChangeCallback callback = (file1) -> {
			LoggerType.DUODUO_CFG_READER.debug("Cfg file [{}] changing", file1.getPath());
			synchronized (this) {
				needReloadCfgs.add(this);
			}

			if (needReloadCfg.compareAndSet(false, true)) {
				TimerManager.instance.scheduleWithDelay(() -> {
					try {
						this.handlerReload();
					}finally {
						needReloadCfgs.clear();
					}
					return null;
				}, 2, TimeUnit.SECONDS);
			}
		};

		for (File file : files) {
			FileUtil.changeListener(file, callback);
		}
	}

	private synchronized void handlerReload() {
		if (needReloadCfg.compareAndSet(true, false)) {
			// 有加载顺序问题. 统一都加载

			LoggerType.DUODUO_CFG_READER.error("=======================文件热加载开始=======================");
			CfgManagers.getInstance().reloadSetting(Lists.newArrayList(needReloadCfgs));
			LoggerType.DUODUO_CFG_READER.error("=======================文件热加载结束=======================");
		}
	}

	@Override
	public String getLoadFileName() {
		return fileName;
	}

	@Override
	public Class<Cfg> getCfgClass() {
		return cfgClass;
	}

	@Override
	public int order() {
		return order;
	}

	/***
	 * 检查cfg class 不能有set方法
	 */
	private void checkCfgClass() {
		for (Field field : cfgClass.getDeclaredFields()) {
			if (isInvalidField(field)) {
				continue;
			}
			boolean haveMethod = true;
			try {
				if (ICfgDelayLoadData.class.isAssignableFrom(field.getType())) {
					delayLoadFields.add(field);
				}
				getSetMethod(field);
			} catch (NoSuchMethodException e) {
				haveMethod = false;
			}

			if (haveMethod) {
				throw new CustomException("Cfg ["+cfgClass.getName()+"] field ["+field.getName()+"] can not define set method");
			}
		}
		delayLoadFields.trimToSize();
	}

	/**
	 * 判断field的有效性.
	 * @param field
	 * @return
	 */
	private boolean isInvalidField(Field field) {
		return Modifier.isPublic(field.getModifiers())
			|| Modifier.isFinal(field.getModifiers())
			|| Modifier.isStatic(field.getModifiers())
			|| Modifier.isTransient(field.getModifiers());
	}

	/**
	 * 得到对应的set方法
	 * @param field 字段
	 * @throws NoSuchMethodException 没有方法异常. 外面需要捕获
	 */
	private Method getSetMethod(Field field) throws NoSuchMethodException {
		char [] chars = ("set"+field.getName()).toCharArray();
		chars[3] -= 32;
		String methodName = new String(chars);

		return cfgClass.getMethod(methodName, field.getType());
	}

	/***\
	 * 转换字符串为对象. 并且赋值给字段
	 * @param cfg 配置文件对象
	 * @param name 字段名称
	 * @param val 字符串值
	 */
	protected void handlerObjConvertAndAssign(Cfg cfg, String name, String val) {
		Field field = ReflectUtil.findField(cfgClass, name);
		if (field == null) {
			throw new UnknownFieldException(cfgClass.getName(), fileName, name);
		}
		if (isInvalidField(field)) {
			throw new CustomException("Class ["+cfg.getClass().getName()+"] field ["+field.getName()+"] is invalid!");
		}
		Object obj = ConvertManager.getInstance().convert(field, val);
		ReflectUtil.setField(cfg, field, obj);
	}
}
