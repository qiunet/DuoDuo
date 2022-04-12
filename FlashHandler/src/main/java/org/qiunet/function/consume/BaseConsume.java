package org.qiunet.function.consume;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.IThreadSafe;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.utils.exceptions.CustomException;

/**
 * 消耗父类
 * @param <Obj>
 */
public abstract class BaseConsume<Obj extends IThreadSafe> {
	/**
	 * 消耗资源的id
	 */
	protected String cfgId;
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
		this(consumeConfig.getCfgId(), consumeConfig.getValue(), consumeConfig.isBanReplace());
	}

	public BaseConsume(String cfgId, long value) {
		this(cfgId, value, false);
	}

	public BaseConsume(String cfgId, long value, boolean banReplace) {
		this.cfgId = cfgId;
		this.value = value;
		this.banReplace = banReplace;

		Preconditions.checkState(value > 0, "value can not less than 1");
	}

	/**
	 * 校验
	 * @param context 上下文
	 * @return 结果
	 */
	final StatusResult verify(ConsumeContext<Obj> context) {
		if (value < 0) {
			throw new CustomException("Value 小于 0!", value);
		}

		return doVerify(context);
	}

	protected abstract StatusResult doVerify(ConsumeContext<Obj> context);

	/**
	 * 本方法已经记录真实消耗了.
	 * 如果底层有特殊. 则覆盖该方法
	 * @param context 上下文对象
	 */
	public void consume(ConsumeContext<Obj> context) {
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
	public final BaseConsume<Obj> copy() {
		return copy(1);
	}
	/**
	 * 按照倍数 copy
	 *
	 * @param multi
	 * @return
	 */
	public final BaseConsume<Obj> copy(int multi) {
		if (multi * value < 0) {
			throw new CustomException("value {} 和 multi {} 相乘数值溢出!", value, multi);
		}
		return this.doCopy(multi);
	}
	/**
	 * 按照倍数 copy
	 *
	 * @param multi
	 * @return
	 */
	protected abstract BaseConsume<Obj> doCopy(int multi);

	/**
	 * 是否可以合并
	 * @param consume 消耗的具体对象
	 * @return 是否可以合并
	 */
	public boolean canMerge(BaseConsume<Obj> consume) {
	 	return this.getClass() == consume.getClass()
				&& this.getCfgId() == consume.getCfgId()
				&& banReplace == consume.banReplace;
	 }

	/**
	 * 合并一个consume
	 * 子类需要覆盖.
	 * @param consume 消耗的具体对象
	 */
	public final void merge(BaseConsume<Obj> consume) {
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

	public String getCfgId() {
		return cfgId;
	}

	public long getValue() {
		return value;
	}
}
