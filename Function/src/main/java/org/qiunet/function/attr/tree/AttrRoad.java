package org.qiunet.function.attr.tree;

import com.google.common.collect.Maps;
import org.apache.commons.lang.ArrayUtils;
import org.qiunet.utils.async.LazyLoader;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/***
 * 属性节点的路径
 *
 * @author qiunet
 * 2020-11-16 18:42
 */
public class AttrRoad {
	private static final Map<IAttrNodeType, AttrRoad> cache = Maps.newConcurrentMap();
	/**
	 * 对应的节点
	 */
	private AttrNode node;
	/**
	 * 前面的各个节点对应的的
	 */
	private Object[] keys;

	private AttrRoad(AttrNode node) {
		this.node = node;
	}

	public static AttrRoad valueOf(AttrNode node, Object...keys){
		if (keys == null) {
			return cache.computeIfAbsent(node.getNodeType(), key -> new AttrRoad(node));
		}
		AttrRoad nodeRoad = new AttrRoad(node);
		nodeRoad.keys = keys;
		return nodeRoad;
	}

	public AttrNode getNode() {
		return node;
	}

	public IAttrNodeType getNodeType(){
		return node.getNodeType();
	}

	/**
	 * 检查road是否是该路径的子路径
	 * @param road 要检查的路径
	 * @return true 是子路径
	 */
	public boolean isParentOfRoad(AttrRoad road) {
		if (ArrayUtils.getLength(road.keys) < ArrayUtils.getLength(this.keys)) {
			return false;
		}

		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				// 对比当前road的keys是否满足. road的keys可能更多.
				if (! keys[i].equals(road.keys[i])) {
					return false;
				}
			}
		}

		AttrNode node = road.getNode();
		do {
			if (node == this.node) {
				return true;
			}
			node = node.getParent();
		}while (node != null);
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AttrRoad that = (AttrRoad) o;
		return node.getId() == that.node.getId() &&
			Arrays.equals(keys, that.keys);
	}
	private LazyLoader<Integer> hashCodeLoader = new LazyLoader<>(()-> {
		int hashCode = Objects.hash(node.getId());
		hashCode = 31 * hashCode + Arrays.hashCode(keys);
		return hashCode;
	});
	@Override
	public int hashCode() {
		return hashCodeLoader.get();
	}

	@Override
	public String toString() {
		return "AttrRoad{" +
			"node=" + node.getNodeType().getClass().getSimpleName() + "."+ node.getNodeType() +
			", keys=" + Arrays.toString(keys) +
			", hashCode=" + hashCode() +
			'}';
	}
}
