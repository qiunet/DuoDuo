package org.qiunet.entity2table.service;

import com.google.common.collect.Maps;
import org.qiunet.data.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data.core.support.db.IDatabaseSupport;
import org.qiunet.data.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data.redis.constants.RedisDbConstants;
import org.qiunet.data.util.DbProperties;
import org.qiunet.entity2table.command.Columns;
import org.qiunet.entity2table.command.TableCreateParam;
import org.qiunet.entity2table.command.TableParam;
import org.qiunet.utils.string.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * 对表的操作的类
 *
 * @author qiunet
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
		this.alter(createParam.getTableName(), createParam.isSplitDb(), createParam.isSplitTable(), (databaseSupport, newDbName, newTableName) -> {
			createParam.setDbName(newDbName);
			createParam.setTableName(newTableName);
			databaseSupport.selectOne(sqlPath + "createTable", createParam);
		});
	}

	/**
	 * 查询表是否存在
	 * @param tableName
	 * @param defaultDb 是否是默认的db表
	 * @param splitTable 是否是分库的表
	 * @return
	 */
	public boolean findTableCountByTableName(String tableName, boolean splitTable, boolean defaultDb) {
		if (splitTable) {
			tableName = tableName+"_0";
		}
		String dbName = getDbName(defaultDb);
		List<String> tableNames = dbName2TableNames.computeIfAbsent(dbName, this::findTableNamesByDbName);
		if (tableNames == null) {
			return false;
		}
		return tableNames.contains(tableName);
	}

	private String getDbName(boolean splitDb) {
		String dbName = "";
		if (splitDb) {
			// 只需要找第一个库就行.
			dbName = RedisDbConstants.DB_NAME_PREFIX + 0;
		}else if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)){
			dbName =  ((DefaultDatabaseSupport) DefaultDatabaseSupport.getInstance()).getDbName();
		}
		if (StringUtil.isEmpty(dbName)) {
			throw new RuntimeException(" can not get dbName for query!");
		}
		return dbName;
	}

	private static Map<String, List<String>> dbName2TableNames  = Maps.newHashMap();
	private List<String> findTableNamesByDbName(String dbName) {
		// 从公共库取. 使用默认源就行.
		return DefaultDatabaseSupport.getInstance().selectList(sqlPath + "findTableNamesByDbName", dbName);
	}

	/**
	 * 扫描一个表的列属性
	 * 仅扫描 0库的下标为_0的表
	 * @param tableName
	 * @return
	 */
	public List<Columns> findTableEnsembleByTableName(String tableName, boolean splitTable, boolean splitDb) {
		if (splitTable) {
			tableName = tableName+"_0";
		}

		String dbName = getDbName(splitDb);
		Map<String, List<Columns>> tableColumns = dbName2TableColumns.computeIfAbsent(dbName, this::findTableColumnsByDbName);
		return tableColumns.get(tableName);
	}


	/**dbName -> table -> columnList**/
	private static Map<String, Map<String, List<Columns>>> dbName2TableColumns  = Maps.newHashMap();
	private Map<String, List<Columns>> findTableColumnsByDbName(String dbName) {
		// 从公共库取. 使用默认源就行.
		List<Columns> columns = DefaultDatabaseSupport.getInstance().selectList(sqlPath + "findColumnByDbName", dbName);
		if (columns == null) {
			return Collections.emptyMap();
		}
		return columns.stream().collect(Collectors.groupingBy(Columns::getTable_name));
	}
	/**
	 * 增加列
	 * @param alterParam
	 */
	public void addTableField(TableParam alterParam) {
		this.alter(alterParam.getTableName(), alterParam.isSplitDb(), alterParam.isSplitTable(), (databaseSupport, newDbName, newTableName) -> {
			alterParam.setDbName(newDbName);
			alterParam.setTableName(newTableName);
			databaseSupport.selectOne(sqlPath + "addTableField", alterParam);
		});
	}


	/**
	 * 修改列
	 * @param alterParam
	 */
	public void modifyTableField(TableParam alterParam) {
		this.alter(alterParam.getTableName(), alterParam.isSplitDb(), alterParam.isSplitTable(), (databaseSupport, newDbName, newTableName) -> {
			alterParam.setDbName(newDbName);
			alterParam.setTableName(newTableName);
			databaseSupport.selectOne(sqlPath + "modifyTableField", alterParam);
		});
	}

	/***
	 * Db模式和Cache单数据库模式下, 默认的数据库源. 如果没有. 会取第一个(认为配置里也就一个).
	 */
	private static final String DEFAULT_DATABASE_SOURCE = "default_database_source";
	private void alter(String tableName, boolean splitDb, boolean splitTable, IDbExecutor dbExecutor) {
		String dbName = DbProperties.getInstance().getString(DEFAULT_DATABASE_SOURCE);
		if (! splitDb) {
			if (splitTable) {
				for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
					String tableName0 = tableName + "_" + tbIndex;
					dbExecutor.execute(DefaultDatabaseSupport.getInstance(), dbName, tableName0);
				}
			}else {
				dbExecutor.execute(DefaultDatabaseSupport.getInstance(), dbName, tableName);
			}
		}else if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				dbName = RedisDbConstants.DB_NAME_PREFIX + dbIndex;
				if (splitTable) {
					for (int tbIndex = 0; tbIndex < RedisDbConstants.MAX_TABLE_FOR_TB_SPLIT; tbIndex++) {
						String tableName0 = (tableName + "_" + tbIndex);
						dbExecutor.execute(MoreDbSourceDatabaseSupport.getInstance(dbSource), dbName, tableName0);
					}
				} else {
					dbExecutor.execute(MoreDbSourceDatabaseSupport.getInstance(dbSource), dbName, tableName);
				}
			}
		}
	}


	private interface IDbExecutor {
		/**
		 * 使用指定的源执行sql命令
		 * @param databaseSupport
		 * @param dbName
		 * @param tableName
		 */
		void execute(IDatabaseSupport databaseSupport, String dbName, String tableName);
	}
}
