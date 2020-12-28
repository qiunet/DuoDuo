package org.qiunet.function.consume;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.function.base.IMainObject;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Consumes<Obj extends IMainObject> {
	/**
	 * 主要的消耗内容
	 */
	private final List<AbstractConsume<Obj>> consumeList;

	public Consumes() {
		this(Lists.newArrayListWithCapacity(3));
	}

	public Consumes(List<AbstractConsume<Obj>> consumeList) {
		this.consumeList = consumeList;
	}

	/**
	 * 1倍 消耗校验
	 * @param obj 消耗的主体对象
	 * @param consumeType 消耗的日志类型
	 * @return 消耗上下文
	 */
	public ConsumeContext<Obj> verify(Obj obj, IConsumeType consumeType) {
		return this.verify(obj, 1, consumeType);
	}

	/**
	 * 多倍消耗校验
	 * @param obj 消耗的主体对象
	 * @param multi 倍数
	 * @param consumeType 消耗的日志类型
	 * @return 上下文对象
	 */
	public ConsumeContext<Obj> verify(Obj obj, int multi, IConsumeType consumeType) {
		if (! obj.inSafeThread()) {
			throw new CustomException("Need verify in safe thread!");
		}

		Preconditions.checkArgument(multi >= 1);
		ConsumeContext<Obj> context = ConsumeContext.valueOf(obj, multi, this, consumeType);
		for (AbstractConsume<Obj> consume : consumeList) {
			ConsumeResult result = consume.verify(context);
			if (result.isFail()) {
				context.result = result;
				return context;
			}
		}
		return context;
	}
	/**
	 * 执行消耗
	 * @param context 上下文对象
	 */
	void act(ConsumeContext<Obj> context) {
		if (! context.getObj().inSafeThread()) {
			throw new CustomException("Need verify in safe thread!");
		}

		for (AbstractConsume<Obj> consume : consumeList) {
			consume.consume(context);
		}
	}
	/**
	 * 添加一个 consume
	 * @param consume 上下文对象
	 */
	public void addConsume(AbstractConsume<Obj> consume) {
		boolean merged = false;
		for (AbstractConsume<Obj> abstractConsume : this.consumeList) {
			if (abstractConsume.canMerge(consume)) {
				abstractConsume.doMerge(consume);
				merged = true;
			}
		}
		if (! merged) {
			this.consumeList.add(consume);
		}
	}

	/**
	 * 添加Consumes
	 * @param consumes
	 */
	public void addConsumes(Consumes<Obj> consumes) {
		consumes.consumeList.forEach(this::addConsume);
	}
	/**
	 * 循环遍历.
	 * @param consumer 消耗的consumer
	 */
	public void forEach(Consumer<AbstractConsume<Obj>> consumer, Predicate<AbstractConsume<Obj>> filter) {
		for (AbstractConsume<Obj> objAbstractConsume : consumeList) {
			if (! filter.test(objAbstractConsume)) {
				continue;
			}
			consumer.accept(objAbstractConsume);
		}
	}
	/**
	 * 循环遍历.
	 * @param consumer 消耗的consumer
	 */
	public void forEach(Consumer<AbstractConsume<Obj>> consumer) {
		consumeList.forEach(consumer);
	}

	/**
	 * 是否为空
	 * @return
	 */
	public boolean isEmpty(){
		return consumeList.isEmpty();
	}

	/**
	 * 是否不为空
	 * @return
	 */
	public boolean isNotEmpty(){
		return !isEmpty();
	}
}
