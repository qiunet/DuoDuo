package org.qiunet.data1.support;

import org.qiunet.data1.async.IAsyncNode;
import org.qiunet.data1.core.entity.IEntity;
import org.qiunet.data1.util.DataUtil;

import java.lang.reflect.ParameterizedType;
import java.util.StringJoiner;

/**
 * 基础的DataSupport
 */
 abstract class BaseDataSupport<Po extends IEntity> implements IDataSupport<Po>,IAsyncNode {
	/** 默认的一个Po 查询一些Key  subKey名称*/
 	protected Po defaultPo;
	/**	po的名称. 用来组装 statement */
	protected String poName;
	/** 得到mybatis 需要的nameSpace */
	protected String nameSpace;

	protected String insertStatement;
	protected String updateStatement;
	protected String deleteStatement;
	protected String selectStatement;

	protected BaseDataSupport(){
		try {
			this.init();
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		this.nameSpace = DataUtil.getNameSpace(poName);

		// select 区分 entity 和 list 在子类处理.
		this.selectStatement = nameSpace+".select"+poName;
		this.insertStatement = nameSpace+".insert"+poName;
		this.updateStatement = nameSpace+".update"+poName;
		this.selectStatement = nameSpace+".delete"+poName;

		this.addToAsyncJob();
	}

	/**
	 * 得到po的名称. 用来组装 statement
	 * @return
	 */
	private void init() throws IllegalAccessException, InstantiationException {
		Class clazz = getClass();
		do {
			if (clazz != BaseDataSupport.class) {
				clazz = clazz.getSuperclass();
				continue;
			}

			Class<Po> typeClass = (Class<Po>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
			this.defaultPo = typeClass.newInstance();
			this.poName = typeClass.getSimpleName();
			return;
		}while (clazz != Object.class);

		throw new RuntimeException("class ["+getClass().getName()+"] is not extends BaseDataSupport. can not get PoName");
	}

	/***
	 * 返回缓存的key
	 * @return
	 */
	protected String getCacheKey(Object... keys){
		StringJoiner sj = new StringJoiner("#");
		sj.add(nameSpace);
		for (Object key : keys) {
			sj.add(String.valueOf(key));
		}
		return sj.toString();
	}
}
