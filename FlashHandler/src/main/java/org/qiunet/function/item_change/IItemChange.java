package org.qiunet.function.item_change;

/**
 * 物品变动后的返回给客户端的物品变动信息.
 * @author qiunet
 */
public interface IItemChange {
	/**
	 * 物品变动的ID
	 * @return
	 */
	int getId();

	/**
	 * 物品变动的数量
	 * @return
	 */
	long getCount();
}
