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
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static CreateTableService getInstance() {
		return instance;
	}

	/**
	 * 建表
	 *
	 * @param createParam
	 */
	public void createTable(TableCreateParam createParam) {
		//判断当前项目的数据源是单一数据源还是多个数据源,根据数据源不同,执行不同的处理
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			String tableName = createParam.getTableName();
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
//				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				String dbSource = createParam.getDbSourceName();
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
		} else {
			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "createTable", createParam);
		}
	}

	/**
	 * 查询表是否存在
	 *
	 * @param tableName
	 * @return
	 */
	public int findTableCountByTableName(String dbSourceName, String tableName) {
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
//			String dbSourceName = String.valueOf(0 / RedisDbConstants.DB_SIZE_PER_INSTANCE);
			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + 0);

			return MoreDbSourceDatabaseSupport.getInstance(dbSourceName).selectOne(sqlPath + "findTableCountByTableName", map);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			return DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "findTableCountByTableName", map);
		}
	}

	/**
	 * 扫描一个表的列属性
	 *
	 * @param tableName
	 * @return
	 */
	public List<Columns> findTableEnsembleByTableName(String dbSourceName, String tableName) {

		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
//			String dbSourceName = String.valueOf(0 / RedisDbConstants.DB_SIZE_PER_INSTANCE);

			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + 0);
			return MoreDbSourceDatabaseSupport.getInstance(dbSourceName).selectList(sqlPath + "findTableEnsembleByTableName", map);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			return DefaultDatabaseSupport.getInstance().selectList(sqlPath + "findTableEnsembleByTableName", map);
		}
	}

	/**
	 * 增加列
	 *
	 * @param alterParam
	 */
	public void addTableField(TableAlterParam alterParam) {
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			String tableName = alterParam.getTableName();
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
//				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);

				alterParam.setDbName(RedisDbConstants.DB_NAME_PREFIX + dbIndex);
				if (alterParam.isSplitTable()) {
					for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
						alterParam.setTableName(tableName + "_" + tbIndex);
						MoreDbSourceDatabaseSupport.getInstance(alterParam.getDbSourceName()).selectOne(sqlPath + "addTableField", alterParam);
					}
				} else {
					MoreDbSourceDatabaseSupport.getInstance(alterParam.getDbSourceName()).selectOne(sqlPath + "addTableField", alterParam);
				}
			}
		} else {
			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "addTableField", alterParam);
		}
	}


	/**
	 * 修改列
	 *
	 * @param alterParam
	 */
	public void modifyTableField(TableAlterParam alterParam) {
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			String tableName = alterParam.getTableName();
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
//				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				alterParam.setDbName(RedisDbConstants.DB_NAME_PREFIX + dbIndex);
				if (alterParam.isSplitTable()) {
					for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
						alterParam.setTableName(tableName + "_" + tbIndex);
						MoreDbSourceDatabaseSupport.getInstance(alterParam.getDbSourceName()).selectOne(sqlPath + "modifyTableField", alterParam);
					}
				} else {
					MoreDbSourceDatabaseSupport.getInstance(alterParam.getDbSourceName()).selectOne(sqlPath + "modifyTableField", alterParam);
				}
			}
		} else {
			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "modifyTableField", alterParam);
		}
	}
}
