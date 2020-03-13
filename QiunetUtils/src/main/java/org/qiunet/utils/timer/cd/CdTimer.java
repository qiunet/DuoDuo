package org.qiunet.utils.timer.cd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
	 * 校验cd是否失效
	 * 失效会重新计时 并返回true
	 * @param cdType
	 * @return
	 */
	public void recordCd(ICdType cdType) {
		this.recordCd(cdType, cdType.period(), cdType.unit());
	}
	/***
	 * 使用自己指定的period 记录cd 并返回cd是否失效.
	 * 有时候不一定是使用 cdType 定义的间隔时间.
	 * @param cdType
	 * @param period 自己指定的间隔时间
	 * @param unit 时间单位
	 * @return
	 */
	public void recordCd(ICdType cdType, long period, TimeUnit unit) {
		if (close) {
			return;
		}

		Timer timer = cdTimers.get(cdType);
		if (timer == null) {
			cdTimers.putIfAbsent(cdType, new Timer(unit.toMillis(period)));
			return;
		}
		timer.isTimeout(true);
	}
	/***
	 * 仅仅校验是否cd是否失效.
	 *
	 * @param cdType
	 * @return
	 */
	public boolean isTimeout(ICdType cdType){
		if (close) {
			return true;
		}
		Timer timer = cdTimers.get(cdType);
		if (timer == null) {
			return true;
		}
		return timer.isTimeout(false);
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

	/**
	 * 得到下次cd的毫秒时间戳
	 *
	 * @param cdType
	 * @return 没有cd中, 返回0
	 */
	public long getNextTime(ICdType cdType) {
		if (isTimeout(cdType)) {
			return 0;
		}

		return cdTimers.get(cdType).getNextTime();
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
