package org.qiunet.test.function.test.targets;

import com.google.common.collect.ImmutableList;
import org.qiunet.function.targets.ITargetDef;
import org.qiunet.function.targets.ITargetDefGetter;
import org.qiunet.function.targets.ITargetsDefGetter;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/***
 * 任务目标配置列表
 * 业务自己去写配置convert
 *
 * @author qiunet
 * 2020-11-23 15:08
 */
public class TargetDefList<T extends ITargetDef> implements ITargetsDefGetter<T>, ITargetDefGetter<T> {
	/**
	 * 一个任务需要的所有的目标
	 */
	private final List<T> defs;

	public TargetDefList(List<T> defs) {
		this.defs = ImmutableList.copyOf(defs);
	}


	public TargetDefList(T... defs) {
		this.defs = ImmutableList.copyOf(defs);
	}

	@Override
	public T getTargetDef(int tid) {
		return defs.stream().filter(t -> t.getId() == tid).findFirst().orElseThrow(NullPointerException::new);
	}

	@Override
	public List<T> targets() {
		return defs;
	}
}
