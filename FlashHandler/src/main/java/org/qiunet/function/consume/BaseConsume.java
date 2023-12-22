package org.qiunet.function.consume;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.thread.IThreadSafe;

import java.util.Objects;

/**
 * 消耗父类
 * @param <Obj>
 */
public abstract class BaseConsume<Obj extends IThreadSafe> {
	/**
	 * 消耗的id 可以资源id, 可以指定背包物品id.
	 * 具体的消耗逻辑由子类实现
	 */
	protected Object id;
	/**
	 * 消耗资源的数量
	 */
	protected long count;

	public BaseConsume(Object id, long count) {
		Preconditions.checkState(count > 0, "count can not less than 1");
		this.count = count;
		this.id = id;
	}

	/**
	 * 校验
	 * @param context 上下文
	 * @return 结果
	 */
	final StatusResult verify(ConsumeContext<Obj> context) {
		if (count < 0) {
			throw new CustomException("Count 小于 0!", count);
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
		if (multi * count < 0) {
			throw new CustomException("value {} 和 multi {} 相乘数值溢出!", count, multi);
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
			&& Objects.equals(getId(), consume.getId());
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
		this.count += consume.count;
	}

	public Object getId() {
		return id;
	}

	public long getCount() {
		return count;
	}
}
