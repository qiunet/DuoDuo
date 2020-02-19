package org.qiunet.cfg.convert;

import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;

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
	private List<? extends BaseObjConvert> converts;

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


	@Override
	public void setApplicationContext(IApplicationContext context) {
		this.converts = context.getSubTypesOf(BaseObjConvert.class).stream()
			.map(clazz -> {
				try {
					return clazz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				return null;
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	public List<? extends BaseObjConvert> getConverts() {
		return converts;
	}
}