package org.qiunet.utils.http;

/**
 * Created by qiunet.
 * 18/1/9
 */
public class DefaultHttpUtil extends AbstractHttpUtil {

	private volatile static DefaultHttpUtil instance;

	private DefaultHttpUtil() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static DefaultHttpUtil getInstance() {
		if (instance == null) {
			synchronized (DefaultHttpUtil.class) {
				if (instance == null)
				{
					new DefaultHttpUtil();
				}
			}
		}
		return instance;
	}
}
