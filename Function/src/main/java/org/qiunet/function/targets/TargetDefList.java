package org.qiunet.function.targets;

import java.util.List;
import java.util.function.BiConsumer;

/***
 * 任务目标配置列表
 * 业务自己去写配置convert
 *
 * @author qiunet
 * 2020-11-23 15:08
 */
public class TargetDefList {
	/**
	 * 一个任务需要的所有的目标
	 */
	private List<ITargetDef> defs;

	public TargetDefList(List<ITargetDef> defs) {
		this.defs = defs;
	}

	public ITargetDef get(int index) {
		return defs.get(index);
	}

	public int size(){
		return defs.size();
	}

	public void forEatch(BiConsumer<Integer, ITargetDef> consumer) {
		for (int i = 0; i < defs.size(); i++) {
			consumer.accept(i, defs.get(i));
		}
	}

	public List<ITargetDef> getDefs() {
		return defs;
	}
}
