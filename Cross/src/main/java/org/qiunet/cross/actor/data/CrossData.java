package org.qiunet.cross.actor.data;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/***
 * 跨服获取数据.
 *
 * @author qiunet
 * 2020-10-28 10:50
 */
public abstract class CrossData<Data extends BaseCrossTransferData> {
	private static final Map<String, CrossData> cacheDatas = Maps.newHashMap();
	private Class<Data> clazz;
	private String key;
	public CrossData(String key) {
		if (StringUtil.isEmpty(key)) {
			throw new CustomException("Key is empty string");
		}

		if (cacheDatas.containsKey(key)) {
			throw new CustomException("Key {} is repeated!", key);
		}
		Type superclass = getClass().getGenericSuperclass();
		this.clazz = (Class<Data>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
		cacheDatas.put(key, this);
		this.key = key;
	}

	public Class<Data> getDataClass() {
		return clazz;
	}

	public String getKey() {
		return key;
	}

	/**
	 * 获得对应的crossData
	 * @param key
	 * @return
	 */
	static CrossData get(String key) {
		return cacheDatas.get(key);
	}
	/**
	 * 逻辑服创建 对象.
	 * @param playerActor 自己强转成playerActor
	 * @return
	 */
	public abstract Data create(AbstractPlayerActor playerActor);
}
