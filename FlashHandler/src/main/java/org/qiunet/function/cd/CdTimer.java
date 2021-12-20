package org.qiunet.function.cd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/***
 * Cd 的工具类
 *
 * @author qiunet
 * 2020-03-02 10:57
 ***/
public class CdTimer<T extends Enum<T> & ICdType> {

	private final Map<T, Timer> cdTimers = new ConcurrentHashMap<>();
	/**
	 * 测试环境可以关闭cd
	 */
	private boolean close;
	/**
	 * 校验cd是否失效
	 * 失效会重新计时 并返回true
	 * @param cdType
	 */
	public void recordCd(T cdType) {
		this.recordCd(cdType, cdType.period(), cdType.unit());
	}
	/***
	 * 使用自己指定的period 记录cd 并返回cd是否失效.
	 * 有时候不一定是使用 cdType 定义的间隔时间.
	 * @param cdType
	 * @param period 自己指定的间隔时间
	 * @param unit 时间单位
	 */
	public void recordCd(T cdType, long period, TimeUnit unit) {
		this.recordCd(cdType, period, unit, cdType.limitCount());
	}

	/***
	 * 使用自己指定的period 记录cd 并返回cd是否失效.
	 * 有时候不一定是使用 cdType 定义的间隔时间.
	 * @param cdType
	 * @param period 自己指定的间隔时间
	 * @param unit 时间单位
	 * @param limitCount 单位时间能进行几次
	 */
	public void recordCd(T cdType, long period, TimeUnit unit, int limitCount) {
		if (close) {
			return;
		}

		cdTimers.computeIfAbsent(cdType, key -> new Timer(unit.toMillis(period), limitCount)).recordCd();
	}
	/***
	 * 仅仅校验是否cd是否失效.
	 *
	 * @param cdType
	 * @return
	 */
	public boolean isTimeout(T cdType){
		if (close || ! cdTimers.containsKey(cdType)) {
			return true;
		}
		return cdTimers.get(cdType).isTimeout();
	}

	/**
	 * 得到该剩余秒数
	 * @return
	 */
	public int getLeftSeconds(T cdType){
		if (isTimeout(cdType)) {
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
	public long getNextTime(T cdType) {
		if (isTimeout(cdType)) {
			return 0;
		}

		return cdTimers.get(cdType).getNextTime();
	}

	/**
	 * 是否关闭cdTimer
	 * 测试时候关闭比较合适
	 * @return
	 */
	public boolean isClose() {
		return close;
	}

	/**
	 * 清理某个cd记录
	 * @param cdType
	 */
	public void cleanCd(T cdType) {
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
