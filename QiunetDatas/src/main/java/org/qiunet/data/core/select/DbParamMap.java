package org.qiunet.data.core.select;

import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.redis.util.DbUtil;

import java.util.HashMap;

/***
 * 查询需要多个字段名称和值的对应关系.
 *
 *
 */
public class DbParamMap extends HashMap<String, Object> {

	private DbParamMap(){
		super(4);
	}

	public static DbParamMap create(Table table, String keyName, Object key){
		DbParamMap paramMap = new DbParamMap();
		paramMap.put(keyName, key);
		if (table.splitDb()) {
			paramMap.put("dbName", DbUtil.getDbName(key));
		}
		if (table.splitTable()) {
			paramMap.put("tbIndex", DbUtil.getTbIndex(key));
		}
		return paramMap;
	}
	public DbParamMap put(String key, Object val) {
		super.put(key, val);
		return this;
	}
}
