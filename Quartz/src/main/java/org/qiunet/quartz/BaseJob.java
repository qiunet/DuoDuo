package org.qiunet.quartz;

import org.qiunet.utils.date.DateUtil;
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
	public Boolean doJob(){
		long start = DateUtil.currentTimeMillis();
		try {
			 return doWork();
		}catch (Exception e) {
			logger.error("Job  ["+jobName+"] Exception ", e);
		}
		finally {
			ThreadContextData.removeAll();
			if (logExecInfo()) {
				logger.info("Job ["+jobName+"] exec ["+(DateUtil.currentTimeMillis() - start)+"]");
			}
		}
		return true;
	}
	/***
	 * 调度
	 * @return
	 */
	protected abstract Boolean doWork() throws Exception;

	/***
	 * 是否打印execInfo信息
	 * @return
	 */
	protected boolean logExecInfo(){
		return true;
	}
}
