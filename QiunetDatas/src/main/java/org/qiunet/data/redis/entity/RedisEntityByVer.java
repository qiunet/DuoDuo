package org.qiunet.data.redis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.qiunet.data.support.IEntityBo;

import java.security.Key;

/**
 * Created by zhengjian
 * Date: 2025/4/13.
 * Time: 20:43.
 */
public class RedisEntityByVer<Do extends IRedisEntity<Key, Bo>, Bo extends IEntityBo<Do>> {
	public RedisEntityByVer() {
	}

	public RedisEntityByVer(Do aDo) {
		this.aDo = aDo;
	}

	private Do aDo;

	public Do getaDo() {
		return aDo;
	}

	public void setaDo(Do aDo) {
		this.aDo = aDo;
	}

	/**
	 * 数据版本校验(如果json的字段多于当前po字段，校验失败)
	 */
	@JSONField(serialize = false, deserialize = false)
	boolean redisJsonAndDoVerCheck = false;

	public boolean isRedisJsonAndDoVerCheck() {
		return redisJsonAndDoVerCheck;
	}

	public void setRedisJsonAndDoVerCheck(boolean redisJsonAndDoVerCheck) {
		this.redisJsonAndDoVerCheck = redisJsonAndDoVerCheck;
	}
}
