package org.qiunet.function.cd;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 * 只要请求. 就会留下记录.
 *
 * @author qiunet
 * 2022/2/8 09:39
 */
public class TouchTimer {

	private final Map<Integer, Timer> timers = Maps.newConcurrentMap();

	/**
	 * 以毫秒为单位. 判断ID是否已经在cd内
	 * @param id id
	 * @param cdTime 数值
	 * @param limitCount cd内限制次数
	 * @return 是否在cd中
	 */
	public boolean isCding(int id, int cdTime, TimeUnit unit, int limitCount) {
		Timer timer = timers.computeIfAbsent(id, key -> new Timer(unit.toMillis(cdTime), limitCount));
		boolean timeout = timer.isTimeout();
		timer.recordCd();
		return !timeout;
	}
}
