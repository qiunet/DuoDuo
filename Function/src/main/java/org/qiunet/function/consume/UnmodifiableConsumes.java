package org.qiunet.function.consume;

import com.google.common.collect.ImmutableList;
import org.qiunet.function.base.IMainObject;

import java.util.List;

/***
 * 不可变更的消耗
 *
 * @author qiunet
 * 2020-12-28 11:59
 */
public final class UnmodifiableConsumes<Obj extends IMainObject> extends Consumes<Obj> {
	/**
	 * 创建不可变更修改的消耗
	 * @param consumeList 列表
	 */
	public UnmodifiableConsumes(List<AbstractConsume<Obj>> consumeList) {
		super(ImmutableList.copyOf(consumeList));
	}
}
