package org.qiunet.function.cd;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/***
 * 是时间戳类型的 cd
 * 然后又不是某个CdType, 而是比如某个技能cd, 某个效果cd等
 * 使用技能id 作为key 记录cd
 *
 * @author qiunet
 * 2021-06-11 15:12
 */
public class CdMark<K> {

	private final Map<K, Long> cdMarks = Maps.newConcurrentMap();

	/**
	 * 是否包含某个key
	 * @param key
	 * @return
	 */
	public boolean hasMark(K key) {
		return cdMarks.containsKey(key);
	}

	/**
	 * 刷新cd
	 * @param key
	 * @param time
	 * @param unit
	 */
	public void refreshCd(K key, long time, TimeUnit unit) {
		long expireMillis = cdMarks.getOrDefault(key, 0L);
		cdMarks.merge(key, (System.currentTimeMillis() + unit.toMillis(time)), Long::max);
	}

	/**
	 * 刷新cd
	 * @param key
	 * @param millis
	 */
	public void refreshCd(K key, long millis) {
		this.refreshCd(key, millis, TimeUnit.MILLISECONDS);
	}

	/**
	 * 得到cd还有多久
	 * @param key
	 * @return
	 */
	public long getLastExpireMillis(K key) {
		Long millis = cdMarks.get(key);
		if (millis == null) {
			return 0;
		}
		millis = Math.max(millis - System.currentTimeMillis(), 0);
		if (millis <= 0) {
			this.removeExpire();
		}
		return millis;
	}

	/**
	 * 得到失效的毫秒时间戳
	 * @param key
	 * @return
	 */
	public long getExpireMillis(K key) {
		Long millis = cdMarks.get(key);
		if (millis == null) {
			return 0;
		}
		if (System.currentTimeMillis() > millis) {
			this.removeExpire();
		}
		return millis;
	}
	/**
	 * 是否cd中
	 * @param key
	 * @return
	 */
	public boolean inCd(K key) {
		return System.currentTimeMillis() < cdMarks.getOrDefault(key, 0L);
	}
	/**
	 * 清理过期的
	 */
	public void removeExpire() {
		cdMarks.entrySet().removeIf(en -> en.getValue() < System.currentTimeMillis());
	}

	/**
	 * 遍历所有
	 * @param consumer
	 */
	public void walkAll(BiConsumer<K, Long> consumer) {
		this.removeExpire();
		cdMarks.forEach(consumer);
	}
}
