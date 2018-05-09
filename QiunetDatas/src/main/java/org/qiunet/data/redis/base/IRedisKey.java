package org.qiunet.data.redis.base;

import org.qiunet.data.db.util.DbProperties;

/***
 * 定义redis 的key 需要的接口
 */
public interface IRedisKey {
	/***
	 *由 dbInfoKey 得到redis 的 key
	 * @param dbInfoKey
	 * @return
	 */
	String getKey(Object dbInfoKey);
	/***
	 * 根据多个参数.拼成可以使用的redis key
	 * @param moreParams
	 * @return
	 */
	String getKeyByParams(Object ...moreParams);

	/***
	 * 得到一个对象的异步redis key
	 * @param dbInfoKey
	 * @return
	 */
	String getAsyncKey(Object dbInfoKey);
}
