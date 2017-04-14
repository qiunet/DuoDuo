package org.qiunet.utils.http;

import org.qiunet.utils.properties.LoaderProperties;

/**
 * @author qiunet
 *         Created on 17/1/10 11:33.
 */
public class PoolProperties extends LoaderProperties {
	private static final String filePath ;
	static {
		filePath = PoolProperties.class.getResource("/pool.properties").getPath();
	}
	/***
	 * 要求传入的绝对地址
	 * @param fileName
	 */
	private PoolProperties(String fileName) {
		super(fileName);
	}
	
	private volatile static PoolProperties instance;
	
	public static PoolProperties getInstance() {
		if (instance == null) {
			synchronized (PoolProperties.class) {
				if (instance == null)
				{
					instance = new PoolProperties(filePath);
				}
			}
		}
		return instance;
	}
}
