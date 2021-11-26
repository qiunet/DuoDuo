package org.qiunet.function.attr.tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.function.attr.IAttrChangeObserver;
import org.qiunet.function.attr.buff.IAttrBuff;
import org.qiunet.function.attr.buff.IAttrNodeBuff;
import org.qiunet.function.attr.enums.AttrValueType;
import org.qiunet.function.attr.enums.IAttrEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/***
 * 对应节点的内容
 *
 * @author qiunet
 * 2020-11-18 12:49
 */
public class AttrRoadContent<Attr extends Enum<Attr> & IAttrEnum<Attr>> {
	/**
	 * buff map
	 */
	private final Map<IAttrBuff, IAttrNodeBuff<Attr, ?>> buffMap;

	private final AttrBox<Attr> attrBox;
	/**
	 * 路径
	 */
	private final AttrRoad road;
	/**
	 * buff 变动时候, 需要通知到的road
	 */
	private final Set<AttrRoad> buffRoads;

	/**
	 * 基础属性
	 */
	private Map<Attr, Long> baseAttrMap;
	/**
	 * 经过加成后的属性
	 */
	private Map<Attr, Long> finalAttrMap;

	AttrRoadContent(AttrBox<Attr> attrBox, AttrRoad road) {
		this.road = road;
		this.attrBox = attrBox;
	 	this.buffMap = new HashMap<>(4);
		this.buffRoads = attrBox.roadContentMap.keySet().stream()
			.filter(road::isParentOfRoad).collect(Collectors.toSet());
		this.buffRoads.add(road);
	}

	/**
	 * 增加buff影响路径
	 * @param road 路径
	 */
	public void markRoad(AttrRoad road) {
		writeLock().lock();
		try {
			buffRoads.add(road);
		}finally {
			writeLock().unlock();
		}
	}

	/**
	 * 删除buff影响路径
	 * @param road 路径
	 */
	public void removeRoad(AttrRoad road) {
		writeLock().lock();
		try {
			buffRoads.remove(road);
		}finally {
			writeLock().unlock();
		}
	}

	/**
	 * 清空自身.
	 */
	private void removeThis() {
		writeLock().lock();
		try {
			attrBox.roadContentMap.remove(road);
			attrBox.roadContentMap.forEach((road0, content) -> {
				if (road.isParentOfRoad(road0)) {
					content.removeRoad(road);
				}
			});
		}finally {
			writeLock().unlock();
		}
	}


	/**
	 * 替换路径的属性
	 * @param attrs
	 */
	void replace(Map<Attr, Long> attrs) {
		if (Objects.equals(baseAttrMap, attrs)) {
			//  没有变化.  两个都是null, 也能比较.
			return;
		}

		Map<Attr, Long> newAttrs = null;
		if (attrs != null) {
			newAttrs = Maps.newHashMap(attrs);
			AttrUtil.removeZeroValue(newAttrs);
		}

		Map<Attr, Long> change;
		writeLock().lock();
		try {
			if (attrs == null) {
				removeThis();
			}

			baseAttrMap = newAttrs;
			if (newAttrs != null) {
				Map<Attr, Long> buffAdditions = this.calBuffAdditions(newAttrs);
				AttrUtil.mergeMap(buffAdditions, newAttrs);
			}

			Map<Attr, Long> diff = AttrUtil.diff(finalAttrMap, newAttrs);

			finalAttrMap = newAttrs;
			change = new HashMap<>(diff.size());
			AttrUtil.mergeToBox(attrBox, diff, change);
		}finally {
			writeLock().unlock();
		}

		if (!change.isEmpty()) {
			attrBox.getObserver().syncFire(IAttrChangeObserver.class, o -> o.attrChange(this.road, change));
		}
	}

	/**
	 * 替换buff
	 * @param buffType buff 类型
	 * @param buffData buff 数据
	 */
	void replaceBuff(IAttrBuff buffType, IAttrNodeBuff<Attr, ?> buffData) {
		Preconditions.checkState(road.getNode().containsBuff(buffType), "Attr node not contains this buff type [%s]", buffType);

		writeLock().lock();
		try {
			IAttrNodeBuff<Attr, ?> oldBuffData;
			if (buffData == null) {
				oldBuffData = buffMap.remove(buffType);
			}else {
				oldBuffData = buffMap.put(buffType, buffData);
			}

			for (AttrRoad buffRoad : buffRoads) {
				AttrRoadContent<Attr> content = attrBox.getRoadContent(buffRoad);
				if (content.baseAttrMap == null || content.baseAttrMap.isEmpty()) {
					continue;
				}

				Map<Attr, Long> oldBuffAdditions = calBuff(content, oldBuffData);
				Map<Attr, Long> newBuffAdditions = calBuff(content, buffData);

				Map<Attr, Long> diff = AttrUtil.diff(oldBuffAdditions, newBuffAdditions);
				if (diff.isEmpty()) {
					continue;
				}

				AttrUtil.mergeMap(diff, content.finalAttrMap);

				Map<Attr, Long> change = new HashMap<>(diff.size());
				AttrUtil.mergeToBox(attrBox, diff, change);

				attrBox.getObserver().syncFire(IAttrChangeObserver.class, o -> o.attrChange(buffRoad, change));
			}
		}finally {
			writeLock().unlock();
		}
	}

	/**
	 * 属性buff 对 content基础属性的加成
	 * @param content road 内容
	 * @param buffData buff数据
	 * @return 加成的数据
	 */
	private Map<Attr, Long> calBuff(AttrRoadContent<Attr> content, IAttrNodeBuff<Attr, ?> buffData){
		if (buffData == null) {
			return null;
		}

		Map<Attr, Long> retMap = new HashMap<>(content.baseAttrMap.size());
		content.baseAttrMap.forEach((type, val) -> retMap.merge(type, buffData.calBuffVal(type, val), Long::sum));
		return retMap;
	}
	/**
	 * 计算buff的值 以及各个父节点buff对该属性的影响.
	 * @param origin 属性
	 * @return 加成的属性
	 */
	private Map<Attr, Long> calBuffAdditions(Map<Attr, Long> origin) {
		Map<Attr, Long> retMap = Maps.newHashMapWithExpectedSize(origin.size());
		attrBox.roadContentMap.forEach((road0, content) -> {
			if (! road0.isParentOfRoad(this.road)) {
				return;
			}

			origin.forEach((type, val) -> {
				if (type.valueType() != AttrValueType.base) {
					return;
				}

				for (IAttrNodeBuff<Attr, ?> nodeBuff : content.buffMap.values()) {
					long buffVal = nodeBuff.calBuffVal(type, val);
					retMap.merge(type, buffVal, Long::sum);
				}
			});
		});

		return retMap;
	}

	/**
	 * 获得节点路径最终属性
	 * @return
	 */
	public Map<Attr, Long> getFinalAttrMap() {
		readLock().lock();
		try {
			return Maps.newHashMap(finalAttrMap);
		}finally {
			readLock().unlock();
		}
	}

	/**
	 * 获得节点路径基础属性
	 * @return
	 */
	public Map<Attr, Long> getBaseAttrMap(){
		readLock().lock();
		try {
			return Maps.newHashMap(baseAttrMap);
		}finally {
			readLock().unlock();
		}
	}

	private Lock writeLock(){
		return attrBox.writeLock;
	}
	private Lock readLock(){
		return attrBox.readLock;
	}
}
