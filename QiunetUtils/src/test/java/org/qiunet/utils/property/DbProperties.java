package org.qiunet.utils.property;

import org.qiunet.utils.properties.anno.DProperties;
import org.qiunet.utils.properties.anno.DPropertiesValue;

/**
 * @author qiunet
 *         Created on 17/1/5 16:55.
 */
@DProperties("db.properties")
public class DbProperties {
	/**
	 * 测试内容
	 */
	@DPropertiesValue("content")
	private static String content;
	/**
	 * 最大数
	 */
	@DPropertiesValue("db_max_count")
	private int dbMaxCount;

	public int getDbMaxCount() {
		return dbMaxCount;
	}

	public static String getContent() {
		return content;
	}
}
