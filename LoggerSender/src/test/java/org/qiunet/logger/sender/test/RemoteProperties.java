package org.qiunet.logger.sender.test;

import org.qiunet.utils.properties.LoaderProperties;

public class RemoteProperties  extends LoaderProperties {
	/***
	 * 要求传入的绝对地址
	 */
	public RemoteProperties() {
		super("remote.properties");
		instance = this;
	}

	private volatile static RemoteProperties instance;


	public static RemoteProperties getInstance() {
		if (instance == null) {
			synchronized (RemoteProperties.class) {
				if (instance == null)
				{
					new RemoteProperties();
				}
			}
		}
		return instance;
	}
}
