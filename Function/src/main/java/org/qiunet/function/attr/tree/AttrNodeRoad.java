package org.qiunet.function.attr.tree;

import com.google.common.collect.Maps;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/***
 * 属性节点的路径
 *
 * @author qiunet
 * 2020-11-16 18:42
 */
public class AttrNodeRoad {
	private static final Map<IAttrNodeType, AttrNodeRoad> cache = Maps.newConcurrentMap();
	/**
	 * 对应的节点
	 */
	private AttrNode node;
	/**
	 * 前面的各个节点对应的的
	 */
	private Object[] keys;

	private AttrNodeRoad(AttrNode node) {
		this.node = node;
	}

	public static AttrNodeRoad valueOf(AttrNode node, Object...keys){
		if (keys == null) {
			return cache.computeIfAbsent(node.getNodeType(), key -> new AttrNodeRoad(node));
		}
		AttrNodeRoad nodeRoad = new AttrNodeRoad(node);
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
	public boolean isParentOfRoad(AttrNodeRoad road) {
		if (ArrayUtils.getLength(road.keys.length) < ArrayUtils.getLength(this.keys.length)) {
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
		AttrNodeRoad that = (AttrNodeRoad) o;
		return node.getId() == that.node.getId() &&
			Arrays.equals(keys, that.keys);
	}
	private int hashCode;
	@Override
	public int hashCode() {
		if (hashCode == 0) {
			hashCode = Objects.hash(node.getId());
			hashCode = 31 * hashCode + Arrays.hashCode(keys);
		}
		return hashCode;
	}
}
