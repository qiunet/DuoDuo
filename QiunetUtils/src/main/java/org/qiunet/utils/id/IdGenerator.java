package org.qiunet.utils.id;

/***
 * 生成id的一个接口
 * 可以是进程有效
 * 或者 redis有效 需要自己实现.
 * @author qiunet
 * 2020-06-27 21:29
 **/
public interface IdGenerator {
	/**
	 * 生成id
	 * @return
	 */
	int makeId();
}
