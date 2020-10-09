package org.qiunet.cross.node;

import com.google.common.collect.Maps;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-10-09 11:21
 */
public enum ServerNodeManager {
	instance;

	private static final Map<Integer, ServerNode> nodes = Maps.newConcurrentMap();

	/**
	 * 添加一个服务器节点
	 * @param node
	 */
	public void addNode(ServerNode node) {
		nodes.put(node.getServerId(), node);
	}

	void refreshServerInfo
}
