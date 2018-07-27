package org.qiunet.quartz;

import org.qiunet.utils.threadLocal.ThreadContextData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet on 4/12/17.
 */
public abstract class BaseJob implements IJob {
	protected final static Logger logger = LoggerFactory.getLogger(BaseJob.class);
	protected String jobName = getClass().getSimpleName();

	/**
	 * 执行工作调度
	 */
	@Override
	public void doJob(){
		long start = System.currentTimeMillis();
		String execInfo = "";
		try {
			 execInfo = doWork();
		}catch (Exception e) {
			logger.error("Job  ["+jobName+"] Exception ", e);
		}
		finally {
			ThreadContextData.removeAll();
			if (logExecInfo()) {
				logger.info("Job ["+jobName+"] exec ["+(System.currentTimeMillis() - start)+"] ms execInfo:"+execInfo);
			}
		}
	}
	/***
	 * 调度
	 * @return
	 */
	protected abstract String doWork() throws Exception;

	/***
	 * 是否打印execInfo信息
	 * @return
	 */
	protected boolean logExecInfo(){
		return true;
	}
}
