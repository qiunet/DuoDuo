package org.qiunet.entity2table.service;

import org.qiunet.entity2table.command.Columns;
import org.qiunet.data.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data.redis.constants.RedisDbConstants;
import org.qiunet.data.util.DbProperties;
import org.qiunet.entity2table.command.FieldParam;
import org.qiunet.entity2table.command.TableAlterParam;
import org.qiunet.entity2table.command.TableCreateParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CreateTableService {
	private static final String sqlPath = "org.qiunet.entity2table.service.CreateTableService.";
	private volatile static CreateTableService instance = new CreateTableService();

	private CreateTableService() {
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
		}
		instance = this;
	}

	public static CreateTableService getInstance() {
		return instance;
	}

	/**
	 * 建表
	 * @param createParam
	 */
	public void createTable(TableCreateParam createParam) {
		//判断当前项目的数据源是单一数据源还是多个数据源,根据数据源不同,执行不同的处理
		String tableName = createParam.getTableName();
		if (createParam.isDefaultDb()) {
			if (createParam.isSplitTable()) {
				for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
					createParam.setTableName(tableName + "_" + tbIndex);
					DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "createTable", createParam);
				}
			} else {
				DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "createTable", createParam);
			}
		} else if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);

				createParam.setDbName(RedisDbConstants.DB_NAME_PREFIX + dbIndex);
				if (createParam.isSplitTable()) {
					for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
						createParam.setTableName(tableName + "_" + tbIndex);
						MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "createTable", createParam);
					}
				} else {
					MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "createTable", createParam);
				}
			}
		}
	}

	/**
	 * 查询表是否存在
	 * @param tableName
	 * @param defaultDb 是否是默认的db表
	 * @param splitTable 是否是分库的表
	 * @return
	 */
	public int findTableCountByTableName(String tableName, boolean splitTable, boolean defaultDb) {
		Map<String, Object> map = new HashMap<>();
		if (splitTable) {
			map.put("tableName", tableName+"_0");
		} else{
			map.put("tableName", tableName);
		}

		if (defaultDb) {
			return DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "findTableCountByTableName", map);
		}else if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			String dbSource = String.valueOf(0 / RedisDbConstants.DB_SIZE_PER_INSTANCE);
			// 只需要找第一个库就行.
			map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + 0);

			return MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "findTableCountByTableName", map);
		}
		return -1;
	}

	/**
	 * 扫描一个表的列属性
	 * @param tableName
	 * @return
	 */
	public List<Columns> findTableEnsembleByTableName(String tableName, boolean splitTable, boolean defaultDb) {
		Map<String, Object> map = new HashMap<>();
		if (splitTable) {
			map.put("tableName", tableName+"_0");
		}else {
			map.put("tableName", tableName);
		}

		if (defaultDb) {
			return DefaultDatabaseSupport.getInstance().selectList(sqlPath + "findTableEnsembleByTableName", map);
		} else if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			String dbSource = String.valueOf(0 / RedisDbConstants.DB_SIZE_PER_INSTANCE);

			map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + 0);
			return MoreDbSourceDatabaseSupport.getInstance(dbSource).selectList(sqlPath + "findTableEnsembleByTableName", map);
		}
		return null;
	}

	/**
	 * 增加列
	 * @param alterParam
	 */
	public void addTableField(TableAlterParam alterParam) {
		String tableName = alterParam.getTableName();
		if (alterParam.isDefaultDb()) {
			if (alterParam.isSplitTable()) {
				for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
					alterParam.setTableName(tableName + "_" + tbIndex);
					DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "addTableField", alterParam);
				}
			}else {
				DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "addTableField", alterParam);
			}

		}else if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				alterParam.setDbName(RedisDbConstants.DB_NAME_PREFIX + dbIndex);
				if (alterParam.isSplitTable()) {
					for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
						alterParam.setTableName(tableName + "_" + tbIndex);
						MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "addTableField", alterParam);
					}
				} else {
					MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "addTableField", alterParam);
				}
			}
		}
	}


	/**
	 * 修改列
	 * @param alterParam
	 */
	public void modifyTableField(TableAlterParam alterParam) {
		String tableName = alterParam.getTableName();
		if (alterParam.isDefaultDb()) {
			if (alterParam.isSplitTable()) {
				for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
					alterParam.setTableName(tableName + "_" + tbIndex);
					DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "modifyTableField", alterParam);
				}
			}else {
				DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "modifyTableField", alterParam);
			}
		}else if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				alterParam.setDbName(RedisDbConstants.DB_NAME_PREFIX + dbIndex);
				if (alterParam.isSplitTable()) {
					for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
						alterParam.setTableName(tableName + "_" + tbIndex);
						MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "modifyTableField", alterParam);
					}
				} else {
					MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "modifyTableField", alterParam);
				}
			}
		}
	}
}
