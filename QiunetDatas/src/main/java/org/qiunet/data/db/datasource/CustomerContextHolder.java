package org.qiunet.data.db.datasource;

public class CustomerContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	public static String getCustomerType() {
		return (String) contextHolder.get();
	}
	/**
	 * 通过字符串选择数据源
	 * @param customerType dbSourceType
	 */
	public static void setCustomerType(String customerType) {
		contextHolder.set(customerType);
	}
}
