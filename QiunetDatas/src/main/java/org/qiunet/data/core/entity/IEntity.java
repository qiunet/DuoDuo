package org.qiunet.data.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.qiunet.data.redis.util.DbUtil;

/***
 * 主键对应一条数据的对象.
 * 一 对 一
 * @param <Key>
 */
public interface IEntity<Key> {
	/***
	 * 得到Entity的主键
	 * 一般是uid  openId
	 * 或者工会id等
	 * @return
	 */
	Key key();

	/***
	 * 主键的字段名
	 * @return
	 */
	String keyFieldName();
	/**
	 * 如果分表的话.
	 * 得到 tbIndex
	 * (0 - 9)
	 * @return
	 */
	@JSONField(serialize= false, deserialize = false)
	default int getTbIndex(){
		return DbUtil.getTbIndex(key());
	}
}
