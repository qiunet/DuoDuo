package org.qiunet.data1.support;

import org.qiunet.data1.async.IAsyncNode;
import org.qiunet.data1.core.entity.IEntity;
import org.qiunet.data1.util.DataUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;


/**
 * 基础的DataSupport
 */
 abstract class BaseDataSupport<Po extends IEntity, Vo extends IEntityVo<Po>> implements IDataSupport<Po, Vo>,IAsyncNode {
 	protected static final Logger logger = LoggerType.DUODUO.getLogger();
	protected VoSupplier<Po , Vo> supplier;
	protected Class<Po> poClass;
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

	protected BaseDataSupport(Class<Po> poClass, VoSupplier<Po, Vo> supplier){
		DataSupportMapping.addMapping(poClass, this);
		this.poClass = poClass;
		this.supplier = supplier;
		try {
			this.init(poClass);
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		this.nameSpace = DataUtil.getNameSpace(poName);

		// select 区分 entity 和 list 在子类处理.
		this.selectStatement = nameSpace+".select"+poName;
		this.insertStatement = nameSpace+".insert"+poName;
		this.updateStatement = nameSpace+".update"+poName;
		this.deleteStatement = nameSpace+".delete"+poName;

		this.addToAsyncJob();
	}

	/**
	 * 得到po的名称. 用来组装 statement
	 * @return
	 */
	private void init(Class<Po> poClass) throws IllegalAccessException, InstantiationException {
		this.defaultPo = poClass.newInstance();
		this.poName = poClass.getSimpleName();
	}
//
//	/***
//	 * 返回缓存的key
//	 * @return
//	 */
//	protected String getCacheKey(Object... keys){
//		StringJoiner sj = new StringJoiner("#");
//		sj.add(nameSpace);
//		for (Object key : keys) {
//			sj.add(String.valueOf(key));
//		}
//		return sj.toString();
//	}
}
