package org.qiunet.data.support;

import org.qiunet.data.async.IAsyncNode;
import org.qiunet.data.core.entity.IEntity;
import org.qiunet.data.core.support.db.DbSourceDatabaseSupport;
import org.qiunet.data.core.support.db.IDatabaseSupport;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.redis.util.DbUtil;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;


/**
 * 基础的DataSupport
 */
 abstract class BaseDataSupport<Key, Do extends IEntity<Key>, Bo extends IEntityBo<Do>> implements IDataSupport<Key, Do, Bo>, IAsyncNode {
 	protected static final Logger logger = LoggerType.DUODUO.getLogger();
	protected BoSupplier<Do, Bo> supplier;
	protected Class<Do> doClass;
	/** 默认的一个Do 查询一些Key  subKey名称*/
	/**	po的名称. 用来组装 statement */
	protected String doName;
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

		 // select 区分 entity 和 list 在子类处理.
		 this.selectAllStatement = DbUtil.getSelectAllStatement(doName);
		 this.selectStatement = DbUtil.getSelectStatement(doName);
		 this.insertStatement = DbUtil.getInsertStatement(doName);
		 this.updateStatement = DbUtil.getUpdateStatement(doName);
		 this.deleteStatement = DbUtil.getDeleteStatement(doName);

		this.addToAsyncJob();
	}
	@Override
	public Bo convertBo(Do aDo) {
		return supplier.get(aDo);
	}
	/**
	 * 得到po的名称. 用来组装 statement
	 * @return
	 */
	private void init(Class<Do> doClass) throws IllegalAccessException, InstantiationException {
		this.doName = doClass.getSimpleName();

		this.table = doClass.getAnnotation(Table.class);
		this.async = table.async();
	}

	/**
	 * 根据 注解Table 获取数据源
	 * @return
	 */
	private final LazyLoader<IDatabaseSupport> databaseSupport = new LazyLoader<>(() -> DbSourceDatabaseSupport.getInstance(this.table.dbSource()));
	public IDatabaseSupport databaseSupport() {
		return databaseSupport.get();
	}
}
