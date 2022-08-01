package org.qiunet.utils.collection.wheel;

import com.google.common.collect.Lists;
import org.qiunet.utils.collection.enums.ForEachResult;
import org.qiunet.utils.common.functional.IndexForeach;

import java.util.List;

/***
 * 环形列表
 *
 * @author qiunet
 * 2022/8/2 17:21
 */
public class WheelList<E> {
	/**
	 * 真实列表内容
	 */
	private final List<Node<E>> list;

	public WheelList(List<E> list) {
		this.list = Lists.newArrayList();
		if (list == null) {
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			Node<E> node = new Node<>(i, list.get(i));
			this.list.add(node);

			if (i > 0) {
				this.list.get(i - 1).next = node;
			}

			if (i == list.size() - 1) {
				node.next = this.list.get(0);
			}
		}
	}
	/**
	 * 从pos 下一个开始循环一圈查找
	 * @param preIndex 从preIndex的下一个数据开始查找
	 * @param consumer 消费者
	 * @return 最后的index
	 */
	public int nextForeach(int preIndex, IndexForeach<E> consumer) {
		Node<E> eNode = this.list.get(preIndex);
		return foreach(eNode.next.pos, consumer);
	}
	/**
	 * 从pos 开始循环一圈查找
	 * @param startIndex 开始查找的Index
	 * @param consumer 消费者
	 * @return 最后的index
	 */
	public int foreach(int startIndex, IndexForeach<E> consumer) {
		Node<E> eNode = this.list.get(startIndex);
		do {
			if (consumer.consume(eNode.pos, eNode.e) == ForEachResult.BREAK) {
				break;
			}
			eNode = eNode.next;
		}while (eNode.pos != startIndex);
		return eNode.pos;
	}



	private static class Node<E> {

		private final int pos;

		private final E e;
		/**
		 * 后一个
		 */
		private Node<E> next;


		public Node(int pos, E e) {
			this.pos = pos;
			this.e = e;
		}
	}
}
