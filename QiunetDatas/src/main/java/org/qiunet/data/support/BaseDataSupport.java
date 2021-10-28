package org.qiunet.data.support;

import org.qiunet.data.async.IAsyncNode;
import org.qiunet.data.core.entity.IEntity;
import org.qiunet.data.core.support.db.DbSourceDatabaseSupport;
import org.qiunet.data.core.support.db.IDatabaseSupport;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.redis.util.DbUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;


/**
 * 基础的DataSupport
 */
 abstract class BaseDataSupport<Do extends IEntity, Bo extends IEntityBo<Do>> implements IDataSupport<Do, Bo>,IAsyncNode {
 	protected static final Logger logger = LoggerType.DUODUO.getLogger();
	protected BoSupplier<Do, Bo> supplier;
	protected Class<Do> doClass;
	/** 默认的一个Do 查询一些Key  subKey名称*/
 	protected Do defaultDo;
	/**	po的名称. 用来组装 statement */
	protected String doName;
	/** 得到mybatis 需要的nameSpace */
	protected String nameSpace;
	/**是否是异步*/
	protected boolean async;
	protected Table table;

	protected String insertStatement;
	protected String updateStatement;
	protected String deleteStatement;
	protected String selectStatement;
	protected String selectAllStatement;

	 BaseDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier){
		DataSupportMapping.addMapping(doClass, this);
		this.doClass = doClass;
		this.supplier = supplier;
		try {
			this.init(doClass);
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		this.nameSpace = DbUtil.getNameSpace(doName);

		// select 区分 entity 和 list 在子类处理.
		this.selectAllStatement = nameSpace+".selectAll"+ doName;
		this.selectStatement = nameSpace+".select"+ doName;
		this.insertStatement = nameSpace+".insert"+ doName;
		this.updateStatement = nameSpace+".update"+ doName;
		this.deleteStatement = nameSpace+".delete"+ doName;

		this.addToAsyncJob();
	}

	/**
	 * 得到po的名称. 用来组装 statement
	 * @return
	 */
	private void init(Class<Do> doClass) throws IllegalAccessException, InstantiationException {
		this.defaultDo = doClass.newInstance();
		this.doName = doClass.getSimpleName();

		this.table = doClass.getAnnotation(Table.class);
		this.async = table.async();
	}

	/**
	 * 根据 注解Table 获取数据源
	 * @return
	 */
	protected IDatabaseSupport databaseSupport() {
		return DbSourceDatabaseSupport.getInstance(this.table.dbSource());
	}
}
