package org.qiunet.utils.timer.cd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * Cd 的工具类
 *
 * @author qiunet
 * 2020-03-02 10:57
 ***/
public class CdTimer {

	private Map<ICdType, Timer> cdTimers = new ConcurrentHashMap<>();
	/**
	 * 测试环境可以关闭cd
	 */
	private boolean close;
	/**
	 * 校验cd是否失效,  如果失效, 重新cd
	 * @return
	 */
	public boolean validCDTimeout(ICdType cdType) {
		return this.validCDTimeout(cdType, true);
	}
	/**
	 * 校验cd是否失效
	 * @param cdType
	 * @param renew true 如果失效, 重新cd,
	 *                false 仅仅校验是否失效
	 * @return
	 */
	public boolean validCDTimeout(ICdType cdType, boolean renew) {
		if (close) {
			return true;
		}

		Timer timer = cdTimers.get(cdType);
		if (timer == null) {
			cdTimers.putIfAbsent(cdType, new Timer(cdType));
			return true;
		}
		return timer.isTimeout(renew);
	}

	/**
	 * 得到该剩余秒数
	 * @return
	 */
	public int getLeftTime(ICdType cdType){
		if (! cdTimers.containsKey(cdType)) {
			return 0;
		}
		Timer timer = cdTimers.get(cdType);
		return timer.getLeftSeconds();
	}

	public boolean isClose() {
		return close;
	}


	public void removeCd(ICdType cdType) {
		cdTimers.remove(cdType);
	}
	/***
	 * 测试环境可以关闭cd
	 */
	public void setClose() {
		this.cdTimers.clear();
		this.close = true;
	}
}
