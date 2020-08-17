package org.qiunet.entity2table.service;

import com.google.common.collect.Maps;
import org.qiunet.data.core.support.db.DbSourceDatabaseSupport;
import org.qiunet.data.core.support.db.IDatabaseSupport;
import org.qiunet.data.redis.util.DbUtil;
import org.qiunet.entity2table.command.Columns;
import org.qiunet.entity2table.command.TableCreateParam;
import org.qiunet.entity2table.command.TableParam;

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
		this.alter(createParam.getTableName(), createParam.getDbSource(), createParam.isSplitTable(), (databaseSupport, newDbName, newTableName) -> {
			createParam.setTableName(newTableName);
			databaseSupport.selectOne(sqlPath + "createTable", createParam);
		});
	}

	/**
	 * 查询表是否存在
	 * @param tableName
	 * @param splitTable 是否是分库的表
	 * @return
	 */
	public boolean findTableCountByTableName(String tableName, String dbSourceName, boolean splitTable) {
		if (splitTable) {
			tableName = tableName+"_0";
		}
		List<String> tableNames = dbSourceName2TableNames.computeIfAbsent(dbSourceName, this::findTableNamesByDbSourceName);
		if (tableNames == null) {
			return false;
		}
		return tableNames.contains(tableName);
	}

	private String getDbName(String dbSourceName) {
		return DbSourceDatabaseSupport.getInstance(dbSourceName).dbName();
	}

	private static Map<String, List<String>> dbSourceName2TableNames  = Maps.newHashMap();
	private List<String> findTableNamesByDbSourceName(String dbSourceName) {
		IDatabaseSupport databaseSupport = DbSourceDatabaseSupport.getInstance(dbSourceName);
		// 从公共库取. 使用默认源就行.
		return databaseSupport.selectList(sqlPath + "findTableNamesByDbName", databaseSupport.dbName());
	}

	/**
	 * 扫描一个表的列属性
	 * 仅扫描 0库的下标为_0的表
	 * @param tableName
	 * @return
	 */
	public List<Columns> findTableEnsembleByTableName(String tableName, String dbSourceName, boolean splitTable) {
		if (splitTable) {
			tableName = tableName+"_0";
		}

		Map<String, List<Columns>> tableColumns = dbSourceName2TableColumns.computeIfAbsent(dbSourceName, this::findTableColumnsByDbSourceName);
		return tableColumns.get(tableName);
	}


	/**dbName -> table -> columnList**/
	private static Map<String, Map<String, List<Columns>>> dbSourceName2TableColumns  = Maps.newHashMap();
	private Map<String, List<Columns>> findTableColumnsByDbSourceName(String dbSourceName) {
		IDatabaseSupport databaseSupport = DbSourceDatabaseSupport.getInstance(dbSourceName);
		// 从公共库取. 使用默认源就行.
		List<Columns> columns = databaseSupport.selectList(sqlPath + "findColumnByDbName", databaseSupport.dbName());
		if (columns == null) {
			return Collections.emptyMap();
		}
		return columns.stream().collect(Collectors.groupingBy(Columns::getTable_name));
	}
	/**
	 * 增加列
	 *
	 * ALTER TABLE `qiunet_db`.`equip`
	 * ADD COLUMN `co1` int NULL,
	 * ADD COLUMN `co2` varchar(255) NULL;
	 *
	 * @param alterParam
	 */
	public void addTableField(TableParam alterParam) {
		this.alter(alterParam.getTableName(), alterParam.getDbSource(), alterParam.isSplitTable(), (databaseSupport, newDbName, newTableName) -> {
			alterParam.setDbName(newDbName);
			alterParam.setTableName(newTableName);
			databaseSupport.selectOne(sqlPath + "addTableField", alterParam);
		});
	}


	/**
	 * 修改列
	 * ALTER TABLE `qiunet_db`.`equip`
	 * MODIFY COLUMN `equip_id` bigint NOT NULL COMMENT '装备id',
	 * MODIFY COLUMN `level` int NOT NULL COMMENT '装备等级';
	 * @param alterParam
	 */
	public void modifyTableField(TableParam alterParam) {
		this.alter(alterParam.getTableName(), alterParam.getDbSource(), alterParam.isSplitTable(), (databaseSupport, newDbName, newTableName) -> {
			alterParam.setDbName(newDbName);
			alterParam.setTableName(newTableName);
			databaseSupport.selectOne(sqlPath + "modifyTableField", alterParam);
		});
	}

	/***
	 * Db模式和Cache单数据库模式下, 默认的数据库源. 如果没有. 会取第一个(认为配置里也就一个).
	 */
	private void alter(String tableName, String dbSource, boolean splitTable, IDbExecutor dbExecutor) {
		String dbName = getDbName(dbSource);
		if (splitTable) {
			for (int tbIndex = 0; tbIndex < DbUtil.getMaxTableForTbSplit(); tbIndex++) {
				String tableName0 = tableName + "_" + tbIndex;
				dbExecutor.execute(DbSourceDatabaseSupport.getInstance(dbSource), dbName, tableName0);
			}
		}else {
			dbExecutor.execute(DbSourceDatabaseSupport.getInstance(dbSource), dbName, tableName);
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
