package org.qiunet.function.consume;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.base.IOperationType;
import org.qiunet.function.base.IResourceType;
import org.qiunet.function.base.basic.IBasicFunction;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.anno.AutoWired;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Consumes<Obj extends IThreadSafe> {
	@AutoWired
	protected static IBasicFunction basicFunction;
	/**
	 * 主要的消耗内容
	 */
	protected List<BaseConsume<Obj>> consumeList;

	public Consumes() {
		this(Lists.newArrayListWithCapacity(3));
	}

	public Consumes(List<BaseConsume<Obj>> consumeList) {
		this.consumeList = consumeList;
	}

	/**
	 * 1倍 消耗校验
	 * @param obj 消耗的主体对象
	 * @param consumeType 消耗的日志类型
	 * @return 消耗上下文
	 */
	public ConsumeContext<Obj> verify(Obj obj, IOperationType consumeType) {
		if (! obj.inSelfThread()) {
			throw new CustomException("Need verify in safe thread!");
		}

		ConsumeContext<Obj> context = ConsumeContext.valueOf(obj, this, consumeType);
		for (BaseConsume<Obj> consume : consumeList) {
			StatusResult result = consume.verify(context);
			if (result.isFail()) {
				context.result = result;
				return context;
			}
		}
		context.result = StatusResult.SUCCESS;
		return context;
	}
	/**
	 * 执行消耗
	 * @param context 上下文对象
	 */
	void act(ConsumeContext<Obj> context) {
		if (! context.getObj().inSelfThread()) {
			throw new CustomException("Need verify in safe thread!");
		}

		for (BaseConsume<Obj> consume : consumeList) {
			consume.consume(context);
		}

		ConsumeEventData.valueOf(context).fireEventHandler();
	}

	/**
	 * 创造倍率的消耗
	 * @param multi 倍数
	 * @return
	 */
	Consumes<Obj> createMulti(int multi) {
		Preconditions.checkState(multi > 0);
		Consumes<Obj> consumes = new Consumes<>();
		this.forEach(item -> consumes.addConsume(item.copy(multi)));
		return consumes;
	}
	/**
	 * 添加消耗
	 * @param cfgId 资源id
	 * @param count 数量
	 */
	public void addConsume(String cfgId, long count) {
		this.addConsume(cfgId, count, false);
	}

	/**
	 * 添加消耗
	 * @param cfgId 资源id
	 * @param count 数量
	 * @param banReplace 禁止替换
	 */
	public void addConsume(String cfgId, long count, boolean banReplace) {
		IResourceType type = basicFunction.getResType(cfgId);
		this.addConsume(type.createConsume(new ConsumeConfig(cfgId, count, banReplace)));
	}
	/**
	 * 添加一个 consume
	 * @param consume 上下文对象
	 */
	public void addConsume(BaseConsume<Obj> consume) {
		boolean merged = false;
		for (BaseConsume<Obj> baseConsume : this.consumeList) {
			if (baseConsume.canMerge(consume)) {
				baseConsume.doMerge(consume);
				merged = true;
				break;
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
	public void forEach(Consumer<BaseConsume<Obj>> consumer, Predicate<BaseConsume<Obj>> filter) {
		for (BaseConsume<Obj> objBaseConsume : consumeList) {
			if (! filter.test(objBaseConsume)) {
				continue;
			}
			consumer.accept(objBaseConsume);
		}
	}
	/**
	 * 循环遍历.
	 * @param consumer 消耗的consumer
	 */
	public void forEach(Consumer<BaseConsume<Obj>> consumer) {
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
