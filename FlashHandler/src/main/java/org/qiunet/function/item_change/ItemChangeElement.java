package org.qiunet.function.item_change;

import com.google.common.base.Preconditions;
import org.qiunet.cfg.manager.base.LoadSandbox;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.function.base.IResourceCfg;
import org.qiunet.function.base.IResourceType;
import org.qiunet.utils.exceptions.CustomException;

/**
 * ItemChangeElement 是一个泛型类，用于表示物品变更的元素。
 * @param <Context>
 */
public abstract class ItemChangeElement<T extends ItemChangeElement<T, Context>, Context> {

	protected boolean unmodifiable;

	protected final int id;

	private long count;

	public ItemChangeElement(int id, long count) {
		Preconditions.checkArgument(count > 0);
		Preconditions.checkArgument(id > 0);
		this.count = count;
		this.id = id;
	}

	void setUnmodifiable() {
		this.unmodifiable = true;
	}

	/**
	 * 判断是否可以合并当前元素与指定元素
	 * @param element
	 * @return
	 */
	protected boolean canMerge(T element) {
		return this.getClass() == element.getClass()
			&& this.id == element.id
			&& !unmodifiable;
	}

	/**
	 * 合并当前元素与指定元素
	 * @param element
	 */
	protected void doMerge(T element) {
		if (unmodifiable) {
			throw new CustomException("Unmodifiable");
		}
		this.count += element.getCount();
	}
	/**
	 * 校验
	 * @param context 上下文
	 * @return 结果.
	 */
	protected abstract StatusResult doVerify(Context context);
	/**
	 * 校验
	 * @param context 上下文
	 * @return 结果.
	 */
	final StatusResult verify(Context context) {
		if (count < 0) {
			throw new CustomException("Count 小于 0!", count);
		}

		return doVerify(context);
	}
	/**
	 * 发放奖励
	 * @param context 奖励上下文
	 */
	public abstract void act(Context context);

	/**
	 * 复制一份
	 * @return
	 */
	public T copy() {
		return copy(1);
	}

	/**
	 * 复制指定数量的Item
	 * @param count
	 * @return
	 */
	protected T copy(int count) {
		return this.doCopy(count);
	}

	/**
	 * 复制指定数量的Item
	 * @param count
	 * @return
	 */
	protected abstract T doCopy(int count);

	/**
	 * 获取元素的ID
	 * @return 元素的ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * 获取元素的数量
	 * @return
	 */
	public long getCount() {
		return count;
	}
	/**
	 * 获得type
	 * @return
	 */
	public <Type extends Enum<Type> & IResourceType> Type resType() {
		IResourceCfg res = LoadSandbox.instance.getResById(id);
		assert res != null;
		return res.type();
	}
}
