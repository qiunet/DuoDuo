package org.qiunet.function.targets;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.BiConsumer;

/***
 * 任务目标配置列表
 * 业务自己去写配置convert
 *
 * @author qiunet
 * 2020-11-23 15:08
 */
public class TargetDefList<T extends ITargetDef> implements ITargetDefGetter<T> {
	/**
	 * 一个任务需要的所有的目标
	 */
	private List<T> defs;

	public TargetDefList(List<T> defs) {
		this.defs = ImmutableList.copyOf(defs);
	}


	public TargetDefList(T... defs) {
		this.defs = ImmutableList.copyOf(defs);
	}

	public T get(int index) {
		return defs.get(index);
	}

	public int size(){
		return defs.size();
	}

	public void forEach(BiConsumer<Integer, T> consumer) {
		for (int i = 0; i < defs.size(); i++) {
			consumer.accept(i, defs.get(i));
		}
	}

	public List<T> getDefs() {
		return defs;
	}

	@Override
	public TargetDefList<T> getTargetCfg() {
		return this;
	}
}
