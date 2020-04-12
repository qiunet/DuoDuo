package org.qiunet.cfg.manager;

import org.qiunet.cfg.base.ICfgManager;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.properties.LoaderProperties;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 总管 游戏设定加载
 * @author qiunet
 *         Created on 17/2/9 12:15.
 */
public class CfgManagers {
	private Logger logger = LoggerType.DUODUO.getLogger();

	private static final CfgManagers instance = new CfgManagers();
	private List<Container<ICfgManager>> gameSettingList = new ArrayList<>();
	private List<Container<LoaderProperties>> propertyList = new ArrayList<>();

	private CfgManagers(){
		if (instance != null) {
			throw new IllegalStateException("Already has instance .");
		}
	}

	public static CfgManagers getInstance(){
		if (instance == null) {
			synchronized (CfgManagers.class){
				new CfgManagers();
			}
		}
		return instance;
	}

	/**
	 * 初始化会比重新加载多一层排序
	 */
	public void initSetting() throws Throwable {
		Collections.sort(gameSettingList);
		Collections.sort(propertyList);
		this.reloadSetting();
	}

	/***
	 * 重新加载
	 * @return 返回加载失败的文件名称
	 * @throws Exception
	 */
	public void reloadSetting() throws Throwable {
		logger.error("Game Setting Data Load start.....");
		this.loadPropertySetting();
		this.loadDataSetting();
		logger.error("Game Setting Data Load over.....");
	}

	private Set<Class<? extends ICfgManager>> cfgClasses = new HashSet<>();
	/**
	 * 添加 Manager
	 * @param manager
	 * @param order
	 */
	public void addDataSettingManager(ICfgManager manager, int order) {
		if (cfgClasses.contains(manager.getClass())) {
			return;
		}

		this.gameSettingList.add(new Container<>(manager, order));
		cfgClasses.add(manager.getClass());
	}
	/**
	 * 添加 properties
	 * @param properties
	 * @param order
	 */
	public void addPropertySetting(LoaderProperties properties, int order) {
		this.propertyList.add(new Container<>(properties, order));
	}
	/**
	 * 加载property
	 */
	private void loadPropertySetting() {
		for (Container<? extends LoaderProperties> container : propertyList){
			logger.info("Load Properties ["+ container.t.getClass().getName() +"]");
			container.t.reload();
		}
	}

	/***
	 * 加载设定文件
	 * @return 返回加载失败的文件名称
	 * @throws Exception
	 */
	private void loadDataSetting() throws Throwable {
		int size = gameSettingList.size();
		CountDownLatch latch = new CountDownLatch(size);
		DPromise<Throwable> future = new DCompletePromise<>();
		for (Container<ICfgManager> container : gameSettingList) {
			DFuture<Void> dFuture = TimerManager.getInstance().executorNow(() -> {
					container.t.loadCfg();
					return null;
			});

			dFuture.whenComplete((res, ex) -> {
				if (ex != null) {
					logger.error("读取配置文件" + container.t.getLoadFileName() + "失败!");
					future.trySuccess(ex);

					for (long i = 0; i < latch.getCount(); i++) {
						latch.countDown();
					}
					return;
				}

				logger.info("Load Game Config Manager["+ container.t.getClass().getName() +"]");
				latch.countDown();
			});
		}

		latch.await();
		if (future.isDone()) {
			throw future.get();
		}
	}

	/***
	 * 得到cfg的数量
	 * @return
	 */
	public int cfgSize(){
		return this.gameSettingList.size();
	}

	/***
	 * 得到properties的数量
	 * @return
	 */
	public int propertySize(){
		return propertyList.size();
	}
	/***
	 * IGameSetting  的包装类 包含排序
	 * @param <T>
	 */
	private static class Container<T> implements Comparable<Container<T>> {
		private T t;
		private int order ;

		Container(T t , int order ){
			this.order = order;
			this.t = t;
		}

		@Override
		public int compareTo(Container<T> o) {
			return o.order - order;
		}
	}
}
