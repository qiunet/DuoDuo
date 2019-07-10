package org.qiunet.data1.core.select;

import java.util.HashMap;

/***
 * 查询需要多个字段名称和值的对应关系.
 *
 *
 */
public class SelectMap extends HashMap<String, Object> {

	private SelectMap(){
		super(4);
	}
	public static SelectMap create(){
		return new SelectMap();
	}
	public SelectMap put(String key, Object val) {
		super.put(key, val);
		return this;
	}
}
