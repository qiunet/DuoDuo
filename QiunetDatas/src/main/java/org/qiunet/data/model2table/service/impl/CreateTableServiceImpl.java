package org.qiunet.data.model2table.service.impl;

import org.qiunet.data.db.core.DatabaseSupport;
import org.qiunet.data.db.util.DbProperties;
import org.qiunet.data.model2table.command.Columns;
import org.qiunet.data.model2table.service.CreateTableService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 艾小天
 * @email :wongtp@outlook.com
 * @date ：2017年12月22日 上午10:03:32
 */
public class CreateTableServiceImpl implements CreateTableService {

	private static final String sqlPath = "org.qiunet.data.model2table.service.CreateTableService.";

	@Override
	public void createTable(Map<String, List<Object>> tableMap, String tableComment) {
		List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("tableComment", tableComment);
			map.put("tableMap", tableMap);

			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "createTable", map);
		}

	}

	@Override
	public int findTableCountByTableName(String tableName) {

		String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(0);

		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableName);
		map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(0));

		return DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "findTableCountByTableName", map);
	}

	@Override
	public List<Columns> findTableEnsembleByTableName(String tableName) {

		String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(0);

		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableName);
		map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(0));
		return DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectList(sqlPath + "findTableEnsembleByTableName", map);
	}

	@Override
	public void addTableField(Map<String, Object> tableMap) {
		List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));

			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "addTableField", map);
		}
	}

	@Override
	public void removeTableField(Map<String, Object> tableMap) {
		List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));

			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "removeTableField", map);
		}
	}

	@Override
	public void modifyTableField(Map<String, Object> tableMap) {
		List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "modifyTableField", map);
		}
	}

	@Override
	public void dropKeyTableField(Map<String, Object> tableMap) {
		List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "dropKeyTableField", map);
		}
	}

	@Override
	public void dropUniqueTableField(Map<String, Object> tableMap) {
		List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);
			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "dropUniqueTableField", map);
		}
	}

	@Override
	public void dorpTableByName(String tableName) {
		List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "dorpTableByName", map);
		}
	}

	@Override
	public Integer getTableCount(String dbSourceKey) {
		Map<String, Object> map = new HashMap<>();
		map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(0));
		return DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "getTableCount", map);
	}

}
