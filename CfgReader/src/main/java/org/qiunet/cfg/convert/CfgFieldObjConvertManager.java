package org.qiunet.cfg.convert;

import com.google.common.collect.Lists;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/***
 * 配置文件里面的字段对象转换器
 *
 * @author qiunet
 * 2020-02-04 13:09
 **/
public class CfgFieldObjConvertManager implements IApplicationContextAware {
	private volatile static CfgFieldObjConvertManager instance;
	/***
	 * 所有的convert
	 */
	private List<? extends BaseObjConvert> converts = Lists.newArrayList();

	private CfgFieldObjConvertManager() {
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
		}
		instance = this;
	}

	public static CfgFieldObjConvertManager getInstance() {
		if (instance == null) {
			synchronized (CfgFieldObjConvertManager.class) {
				if (instance == null)
				{
					new CfgFieldObjConvertManager();
				}
			}
		}
		return instance;
	}
	/***
	 * 按照指定的class 类型转换str
	 * @param clazz
	 * @param val
	 * @return 没有转换器将抛出异常
	 */
	public Object covert(Class clazz, String val) {

		for (BaseObjConvert convert : converts) {
			if (convert.canConvert(clazz)) {
				return convert.fromString(val);
			}
		}

		if (clazz.isEnum() || Enum.class.isAssignableFrom(clazz)) {
			return Enum.valueOf(clazz, val);
		}

		throw new RuntimeException("Can not convert class type for ["+clazz.getName()+"]");
	}

	@Override
	public void setApplicationContext(IApplicationContext context) {
		this.converts = context.getSubTypesOf(BaseObjConvert.class).stream()
			.map(clazz -> (BaseObjConvert)context.getInstanceOfClass(clazz))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	public List<? extends BaseObjConvert> getConverts() {
		return converts;
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}
}
