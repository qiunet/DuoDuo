package org.qiunet.utils.config.property;

import org.qiunet.utils.config.anno.DConfig;
import org.qiunet.utils.config.anno.DConfigValue;

/**
 * @author qiunet
 *         Created on 17/1/5 16:55.
 */
@DConfig(value = "db.properties", listenerChange = true)
public class DbProperties {
	/**
	 * 测试内容
	 */
	@DConfigValue()
	private static String content;
	/**
	 * 最大数
	 */
	@DConfigValue("db_max_count")
	private int dbMaxCount;

	public int getDbMaxCount() {
		return dbMaxCount;
	}

	public static String getContent() {
		return content;
	}
}
