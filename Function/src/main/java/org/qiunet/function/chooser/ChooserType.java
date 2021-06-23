package org.qiunet.function.chooser;

/***
 * 选择器类型
 * 给出一个List<IChooser>
 * 计算出来总的权重, 根据权重  或者 万分比获得一个 IChooser
 * 或者每个IChooser 都随机一次.
 * @author qiunet
 * 2021-06-23 17:02
 */
public enum ChooserType {
	/**
	 * 权重
	 */
	WEIGHT,
	/**
	 * 总万分比
	 */
	TOTAL_RCT,
	/**
	 * 万分比直接随机.
	 */
	RCT_RANDOM,
}
