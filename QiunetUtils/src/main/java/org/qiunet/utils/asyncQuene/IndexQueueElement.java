package org.qiunet.utils.asyncQuene;

/***
 * 给IndexAsyncQueueHandler 单独定义接口
 */
public interface IndexQueueElement extends QueueElement {
	/***
	 * 得到在IndexAsyncQueueHandler中处理使用的index
	 * @return
	 */
	int getQueueIndex();
}
