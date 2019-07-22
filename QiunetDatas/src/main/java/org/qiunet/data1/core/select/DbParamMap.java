package org.qiunet.data1.core.select;

import org.qiunet.data1.redis.util.DbUtil;

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
	public static DbParamMap create(){
		return new DbParamMap();
	}
	public static DbParamMap create(String keyName, Object key){
		DbParamMap paramMap = new DbParamMap();
		paramMap.put(keyName, key);
		paramMap.put("dbName", DbUtil.getDbName(key));
		paramMap.put("tbIndex", DbUtil.getTbIndex(key));
		return paramMap;
	}
	public DbParamMap put(String key, Object val) {
		super.put(key, val);
		return this;
	}
}
