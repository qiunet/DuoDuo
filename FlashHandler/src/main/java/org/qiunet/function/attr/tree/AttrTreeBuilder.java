package org.qiunet.function.attr.tree;

import org.qiunet.function.attr.buff.IAttrBuff;

import java.util.function.Consumer;

/***
 *
 *
 * @author qiunet
 * 2020-11-16 18:23
 */
public class AttrTreeBuilder<NodeEnum extends IAttrNodeType, Buff extends IAttrBuff> {
	/**
	 * 树本身
	 */
	private final AttrTree attrTree;
	/**
	 * 节点本身
	 */
	private final AttrNode parent;

	AttrTreeBuilder(AttrTree attrTree, AttrNode parent) {
		this.attrTree = attrTree;
		this.parent = parent;
	}

	/**
	 * 添加叶子
	 * @return
	 */
	public AttrTreeBuilder<NodeEnum, Buff> addLeaf(NodeEnum nodeEnum, Buff... buffs) {
		return addChildNode(nodeEnum, null, buffs);
	}

	/**
	 * 添加子节点
	 * @param nodeType 节点的类型
	 * @param consumer 子节点需要继续添加的一个接口.
	 * @param buffs buff列表
	 */
	public AttrTreeBuilder<NodeEnum, Buff> addChildNode(NodeEnum nodeType, Consumer<AttrTreeBuilder<NodeEnum, Buff>> consumer, Buff ... buffs) {
		AttrNode childNode = new AttrNode(parent, nodeType, buffs);
		parent.addChildNode(childNode);
		attrTree.addNode(childNode);
		if (consumer != null) {
			consumer.accept(new AttrTreeBuilder(attrTree, childNode));
		}
		return this;
	}

	public AttrTree build(){
		return attrTree;
	}
}
