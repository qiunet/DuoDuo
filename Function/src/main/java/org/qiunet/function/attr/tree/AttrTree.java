package org.qiunet.function.attr.tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.function.attr.buff.IAttrBuff;
import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.utils.id.IntIdGenerator;

import java.util.Map;

/***
 * 根节点的包装类.
 *
 * @author qiunet
 * 2020-11-16 10:01
 */
public class AttrTree {
	/**
	 * 根节点
	 */
	private AttrNode rootNode;
	/**
	 * id
	 */
	private final IntIdGenerator idGenerator = new IntIdGenerator();
	/**
	 * 所有的node
	 */
	private final Map<IAttrNodeType, AttrNode> allNodes = Maps.newHashMap();

	private AttrTree() {}

	/**
	 * builder rootNode
	 * @param nodeType root类型
	 * @return
	 */
	public static <NodeType extends IAttrNodeType, Buff extends IAttrBuff> AttrTreeBuilder<NodeType, Buff> newBuilder(IAttrNodeType nodeType) {
		AttrTree attrTree = new AttrTree();
		attrTree.rootNode = new AttrNode(null, nodeType);
		attrTree.addNode(attrTree.rootNode);
		return new AttrTreeBuilder<>(attrTree, attrTree.rootNode);
	}

	/**
	 * 获得id生成器
	 * @return
	 */
	IntIdGenerator getIdGenerator() {
		return idGenerator;
	}

	/**
	 * rootTree 添加 树
	 * @param attrTree
	 */
	public void merge(AttrTree attrTree) {
		this.rootNode.addChildNode(attrTree.rootNode);
		attrTree.allNodes.forEach((type, node) -> {
			AttrNode oldNode = this.allNodes.put(type, node);
			Preconditions.checkState(oldNode == null, "type %s is exist in AttrTree!", type);
		});
	}

	/**
	 * 添加节点到allNodes
	 * @param attrNode
	 */
	void addNode(AttrNode attrNode) {
		AttrNode oldNode = allNodes.put(attrNode.getNodeType(), attrNode);
		Preconditions.checkState(oldNode == null, "type %s is exist in AttrTree!", attrNode.getNodeType());
	}
	/**
	 * 获得指定的节点.
	 * @param nodeType 节点类型
	 * @return
	 */
	public AttrNode getNode(IAttrNodeType nodeType) {
		Preconditions.checkState(allNodes.containsKey(nodeType), "Not contain node for type [%s]", nodeType);
		return allNodes.get(nodeType);
	}
	/**
	 * 得到AttrBox
	 * @return
	 */
	public <Owner extends AbstractUserActor<Owner>, Attr extends Enum<Attr> & IAttrEnum<Attr>> AttrBox<Owner, Attr> buildAttrBox(Owner owner) {
		return new AttrBox<>(owner, this);
	}

	/**
	 * 打印数
	 */
	public void printTree() {
		rootNode.printNode("");
	}
}
