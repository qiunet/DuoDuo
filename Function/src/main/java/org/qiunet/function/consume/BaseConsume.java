package org.qiunet.function.consume;

import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.utils.exceptions.CustomException;

/**
 * 消耗父类
 * @param <Obj>
 */
public abstract class BaseConsume<Obj extends IThreadSafe> {
	/**
	 * 消耗资源的id
	 */
	protected int cfgId;
	/**
	 * 消耗资源的数量
	 */
	protected long value;
	/**
	 * 不允许替代
	 * 某些货币可以使用其他货币替代. 这里如果是true, 则不允许.
	 */
	protected boolean banReplace;

	public BaseConsume(ConsumeConfig consumeConfig) {
		this(consumeConfig.getCfgId(), consumeConfig.getCount(), consumeConfig.isBanReplace());
	}

	public BaseConsume(int cfgId, long value) {
		this(cfgId, value, false);
	}

	public BaseConsume(int cfgId, long value, boolean banReplace) {
		this.cfgId = cfgId;
		this.value = value;
		this.banReplace = banReplace;
	}

	/**
	 * 校验
	 * @param context 上下文
	 * @return 结果
	 */
	final ConsumeResult verify(ConsumeContext<Obj> context) {
		if (context.getMulti() < 1) {
			throw new CustomException("Multi 数值 {} 不合法, 必须 >= 1", context.getMulti());
		}

		if (value * context.getMulti() < 0) {
			throw new CustomException("Value {} 和 multi {} 相乘后会溢出!", value, context.getMulti());
		}

		return doVerify(context);
	}

	protected abstract ConsumeResult doVerify(ConsumeContext<Obj> context);

	/**
	 * 本方法已经记录真实消耗了.
	 * 如果底层有特殊. 则覆盖该方法
	 * @param context 上下文对象
	 */
	public void consume(ConsumeContext<Obj> context) {
		context.getRealConsumes().merge(cfgId, value*context.getMulti(), Long::sum);
		this.doConsume(context);
	}
	/**
	 * 消耗. 真实消耗由 {@link BaseConsume#consume(ConsumeContext)} 记录.
	 * 实现此方法不需要记录
	 * @param context 上下文对象
	 */
	protected abstract void doConsume(ConsumeContext<Obj> context);

	/**
	 * clone
	 * @return 返回clone的对象
	 */
	abstract BaseConsume<Obj> copy();

	/**
	 * 是否可以合并
	 * @param consume 消耗的具体对象
	 * @return 是否可以合并
	 */
	 boolean canMerge(BaseConsume<Obj> consume) {
	 	return this.getClass() == consume.getClass()
				&& this.getCfgId() == consume.getCfgId()
				&& banReplace == consume.banReplace;
	 }

	/**
	 * 合并一个consume
	 * 子类需要覆盖.
	 * @param consume 消耗的具体对象
	 */
	final void merge(BaseConsume<Obj> consume) {
		if (canMerge(consume)) {
			this.doMerge(consume);
		}
	}

	/**
	 * 合并另一个
	 * @param consume 消耗的具体对象
	 */
	protected void doMerge(BaseConsume<Obj> consume) {
		this.value += consume.value;
	}

	public int getCfgId() {
		return cfgId;
	}

	public long getValue() {
		return value;
	}
}
