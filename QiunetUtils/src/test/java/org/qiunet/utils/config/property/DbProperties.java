package org.qiunet.utils.config.property;

import org.qiunet.utils.config.properties.anno.DProperties;
import org.qiunet.utils.config.properties.anno.DPropertiesValue;

/**
 * @author qiunet
 *         Created on 17/1/5 16:55.
 */
@DProperties("db.properties")
public class DbProperties {
	/**
	 * 测试内容
	 */
	@DPropertiesValue()
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
