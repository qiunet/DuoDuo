package org.qiunet.function.attr.tree;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.qiunet.function.attr.buff.IAttrBuff;
import org.qiunet.utils.id.IntIdGenerator;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/***
 * 属性节点. 游戏启动时候, 就定义好了.
 * 相当于后面AttrBox replace都以该node为准
 *
 * @author qiunet
 * 2020-11-16 17:10
 */
public class AttrNode {
	private static final IntIdGenerator idGenerator = new IntIdGenerator();
	/**
	 * node 的id
	 */
	private int id;

	/**
	 * 外面定义的枚举类型
	 */
	private IAttrNodeType nodeType;
	/**
	 * 父节点.
	 */
	private AttrNode parent;
	/**
	 * buff list
	 */
	private Set<IAttrBuff> buffSet;
	/**
	 * 子node
	 */
	private Map<IAttrNodeType, AttrNode> childNodes;

	AttrNode(AttrNode parent, IAttrNodeType nodeType, IAttrBuff... buffs) {
		this.id = idGenerator.makeId();
		this.nodeType = nodeType;
		this.parent = parent;
		if (buffs != null) {
			buffSet = ImmutableSet.copyOf(buffs);
		}else {
			buffSet = Collections.emptySet();
		}
	}

	/**
	 * 该节点是否有该buff.
	 * @param buff
	 * @return
	 */
	public boolean containsBuff(IAttrBuff buff) {
		return buffSet.contains(buff);
	}
	/**
	 * 添加子节点
	 * @param childNode
	 */
	public void addChildNode(AttrNode childNode) {
		childNode.parent = this;
		if (childNodes == null) {
			childNodes = Maps.newHashMapWithExpectedSize(4);
		}
		childNodes.put(childNode.getNodeType(), childNode);
	}

	/**
	 * 得到父节点
	 * @return
	 */
	public AttrNode getParent() {
		return parent;
	}

	 int getId() {
		return id;
	}

	/**
	 * 得到节点类型
	 * @return
	 */
	public IAttrNodeType getNodeType() {
		return nodeType;
	}

	/**
	 * 构造一个节点路径
	 * 比如: 角色 -> 战神装备 -> 铸魂石玩法 -> 腰带
	 * 其中 角色 -> 战神装备 -> 铸魂石玩法 是确定路径. 腰带是[铸魂石玩法]变量. 则keyClass需要指出腰带的key类型.
	 * keys 第一个参数应该是腰带类型.
	 * 后面的参数. 如果角色有多个角色.战神装备确定. 则第二个参数应该是角色的标识.
	 *
	 * @param keys 路径参数.
	 * @return 节点对应的唯一路径
	 */
	public AttrNodeRoad buildRoad(Object ... keys) {
		int index = keys.length - 1;
		AttrNode node = this;
		while (node != null) {

			node = node.parent;
		}
		return AttrNodeRoad.valueOf(this, keys);
	}

	/**
	 * 是否是指定 node 的父节点.
	 * @param node 指定的node
	 * @return true 表示是父节点.
	 */
	public boolean isParentOfNode(AttrNode node) {
		AttrNode nodeParent = node.parent;
		while (nodeParent != null) {
			if (nodeParent.id == this.id) {
				return true;
			}
			nodeParent = node.parent;
		}
		return false;
	}

	/**
	 * 循环子节点
	 * @param consumer 消费者
	 */
	public void foreachChildNode(Consumer<AttrNode> consumer) {
		childNodes.values().forEach(consumer);
	}
}
