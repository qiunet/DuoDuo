package org.qiunet.profile.reference;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/***
 * 数据参考
 *
 * @author qiunet
 * 2022/8/25 20:54
 */
public class ReferenceData<Key> {

	private final Map<Key, ReferenceRow<Key>> map = Maps.newConcurrentMap();
	/**
	 * 时间
	 */
	private long startDt = System.currentTimeMillis();
	/**
	 * 是否记录
	 */
	private boolean recordEnable;

	public ReferenceData() {
		this(true);
	}

	public ReferenceData(boolean record) {
		this.recordEnable = record;
	}

	public void setRecordEnable(boolean recordEnable) {
		if (! recordEnable) {
			this.reset();
		}
		this.recordEnable = recordEnable;
	}

	public boolean isRecordEnable() {
		return recordEnable;
	}

	/**
	 * 获得某一行数据
	 * @param key
	 * @return
	 */
	public void record(Key key, long data) {
		if (! isRecordEnable()) {
			return;
		}
		map.computeIfAbsent(key, ReferenceRow::new).record(data);
	}

	/**
	 * 打印数据
	 * @param printer 打印器
	 */
	public void print(PrintStream printer) {
		new ReferencePrinter<>(this).print(printer);
		this.reset();
	}

	/**
	 * 获得开始时间
	 * @return
	 */
	long getStartDt() {
		return startDt;
	}

	/**
	 * 重置数据计数
	 */
	public void reset() {
		this.startDt = System.currentTimeMillis();
		this.map.clear();
	}
	/**
	 * 转化为list
	 * @return
	 */
	List<ReferenceRow<Key>> toList() {
		return Lists.newArrayList(map.values());
	}
}
