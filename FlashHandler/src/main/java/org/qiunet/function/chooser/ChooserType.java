package org.qiunet.function.chooser;

import org.qiunet.utils.math.MathUtil;

import java.util.List;
import java.util.function.Consumer;

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
	 * 权重, 把各个权重想加. 得到总权重.
	 * 随机一个数. 确定在哪个区间.
	 */
	WEIGHT {
		@Override
		public <T extends IChooser> void random(List<T> list, Consumer<T> consumer) {
			int sum = list.stream().mapToInt(IChooser::weight).sum();
			ChooserType.select(list, MathUtil.random(sum), consumer);
		}
	},
	/**
	 * 总万分比
	 * 总权重按照 1W算.
	 * 随机一个数. 看落在哪个 IChooser
	 */
	TOTAL_RCT {
		@Override
		public <T extends IChooser> void random(List<T> list, Consumer<T> consumer) {
			ChooserType.select(list, MathUtil.random(10000), consumer);
		}
	},
	/**
	 * 万分比直接随机.
	 * 每个IChooser 按照自己的weight以1W随机,
	 * 随机值小于weight 就可以.
	 */
	RCT_RANDOM {
		@Override
		public <T extends IChooser> void random(List<T> list, Consumer<T> consumer) {
			for (T t : list) {
				if (MathUtil.random(10000) < t.weight()) {
					consumer.accept(t);
				}
			}
		}
	},
	;

	public abstract <T extends IChooser> void random(List<T> list, Consumer<T> consumer);

	/**
	 * 按照随机数. 选择一个IChooser
	 * @param list
	 * @param rand
	 * @param <T>
	 * @return
	 */
	private static <T extends IChooser> void select(List<T> list, int rand, Consumer<T> consumer) {
		int start = 0;
		for (T t : list) {
			int weight = t.weight();
			if (rand >= start && rand < start + weight) {
				consumer.accept(t);
				break;
			}
			start += weight;
		}
	}
}
