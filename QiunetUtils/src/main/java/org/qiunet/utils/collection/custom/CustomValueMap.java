package org.qiunet.utils.collection.custom;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.json.TypeReferences;
import org.qiunet.utils.string.StringUtil;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/***
 * 使用ICustomValue 的Map
 *
 * @author qiunet
 * 2020-12-25 11:58
 */
public class CustomValueMap<DATA extends Enum<DATA> & ICustomValue> {
	/**
	 * 类
	 */
	private final Class<DATA> aClass;
	/**
	 * 存储的数据
	 */
	private final Map<DATA, Object> dataMap = Maps.newConcurrentMap();

	public CustomValueMap(Class<DATA> aClass) {
		Preconditions.checkNotNull(aClass);
		this.aClass = aClass;
	}

	/**
	 * 转成可以持久化的字符串
	 * @return
	 */
	public String toDbString(){
		if (isEmpty()) {
			return "";
		}

		Map<String, String> collect = dataMap.entrySet().stream().collect(Collectors.toMap(en -> en.getKey().name(), en -> en.getValue().toString()));
		return JsonUtil.toJsonString(collect);
	}

	/**
	 * 从字符串加载Map
	 * @param val map的字符串内容
	 */
	public void parse(String val) {
		if (StringUtil.isEmpty(val)) {
			return;
		}

		Map<String, String> map = JsonUtil.getGeneralObj(val, TypeReferences.STRING_STRING_MAP);
		map.forEach((key, data) -> {
			DATA keyData = Enum.valueOf(aClass, key);
			Object valData = keyData.parse(data);
			dataMap.put(keyData, valData);
		});
	}

	@Override
	public String toString() {
		return "CustomValueMap{" +
				"aClass=" + aClass +
				", data=" + toDbString() +
				'}';
	}

	public int size() {
		return dataMap.size();
	}

	public boolean isEmpty() {
		return dataMap.isEmpty();
	}

	public boolean containsKey(DATA key) {
		return dataMap.containsKey(key);
	}

	public <T> T get(DATA key) {
		return (T) dataMap.get(key);
	}

	public <T> T get(DATA key, T defaultVal) {
		return (T) dataMap.getOrDefault(key, defaultVal);
	}

	/**
	 * 没有使用默认值创建一个
	 * @param key
	 * @param <T>
	 * @return
	 */
	public <T> T computeIfAbsent(DATA key) {
		return (T) dataMap.computeIfAbsent(key, key0 -> key0.defaultVal());
	}

	public <T> T put(DATA key, Object value) {
		return (T) dataMap.put(key, value);
	}

	public <T> T remove(DATA key) {
		return (T) dataMap.remove(key);
	}

	public void clear() {
		this.dataMap.clear();
	}

	public Set<DATA> keySet() {
		return dataMap.keySet();
	}

	public void forEach(BiConsumer<DATA, Object> consumer) {
		this.dataMap.forEach(consumer);
	}

	@Override
	public CustomValueMap<DATA> clone() {
		try {
			return (CustomValueMap<DATA>) super.clone();
		} catch (CloneNotSupportedException e) {
			CustomValueMap<DATA> data = new CustomValueMap<>(aClass);
			data.dataMap.putAll(this.dataMap);
			return data;
		}
	}
}
