package org.qiunet.data.async;

import java.util.ArrayList;
import java.util.List;

/**
 * 异步更新的公用类
 * @author qiunet
 *         Created on 17/2/11 08:04.
 */
public class AsyncJobSupport {
	
	private volatile static AsyncJobSupport instance;
	
	private AsyncJobSupport() {
		instance = this;
	}
	
	public static AsyncJobSupport getInstance() {
		if (instance == null) {
			synchronized (AsyncJobSupport.class) {
				if (instance == null)
				{
					new AsyncJobSupport();
				}
			}
		}
		return instance;
	}
	
	private List<AsyncNode> nodes = new ArrayList<>();
	
	public void addNode(AsyncNode node) {
		this.nodes.add(node);
	}
	
	/***
	 * 异步更新到db
	 */
	public void asyncToDb(){
		for (AsyncNode node : nodes) {
			node.updateRedisDataToDatabase();
		}
	}
}
