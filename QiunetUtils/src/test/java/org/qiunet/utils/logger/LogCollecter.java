package org.qiunet.utils.logger;

/**
 * Created by qiunet.
 * 18/2/4
 */
public class LogCollecter extends BaseLogCollecter {

	private volatile static LogCollecter instance;
	private LogCollecter() {
		super("/Users/qiunet/Desktop");
		instance = this;
	}

	public static LogCollecter getInstance() {
		if (instance == null) {
			synchronized (LogCollecter.class) {
				if (instance == null)
				{
					new LogCollecter();
				}
			}
		}
		return instance;
	}

	public void log(String title, Object... args) {
		super.log(title, args);
	}
}
