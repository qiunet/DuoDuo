package org.qiunet.data1.core.select;

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
	public DbParamMap put(String key, Object val) {
		super.put(key, val);
		return this;
	}
}
