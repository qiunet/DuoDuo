package org.qiunet.data.core.support.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/***
 * 本地缓存的一个类
 * @param <Key>
 * @param <Val>
 */
public class LocalCache<Key, Val> {
	/***
	 * 本地存储cache
	 */
	private Cache<Key, Val> localCache;

	private LocalCache() {}

	public static <Key, Val> LocalCache<Key, Val> createTimeExpireCache(){
		LocalCache<Key, Val> localCache = new LocalCache<>();
		localCache.localCache = CacheBuilder.newBuilder()
				.expireAfterAccess(12, TimeUnit.HOURS)
				.softValues()
				.build();
		return localCache;
	}

	public static <Key, Val> LocalCache<Key, Val> createPermanentCache(){
		LocalCache<Key, Val> localCache = new LocalCache<>();
		localCache.localCache = CacheBuilder.newBuilder().build();
		return localCache;
	}

	/**
	 * 从本地缓存取
	 * 没有试用caller 加载
	 * @param key
	 * @param valueLoader
	 * @return
	 * @throws ExecutionException
	 */
	public Val get(Key key, Callable<Val> valueLoader) throws ExecutionException {
		return localCache.get(key, valueLoader);
	}
	/**
	 * 从本地缓存取 可能返回null
	 * @param key
	 * @return
	 * @throws ExecutionException
	 */
	public Val get(Key key) {
		return localCache.getIfPresent(key);
	}
	/***
	 *
	 * @param key
	 * @param val
	 */
	public void put(Key key, Val val) {
		localCache.put(key, val);
	}

	/***
	 * 如果缺失 就put到cache
	 * @param key
	 * @param val
	 */
	public Val putIfAbsent(Key key, Val val) {
		return localCache.asMap().putIfAbsent(key, val);
	}

	/***
	 * Replace
	 * @param key
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public boolean replace(Key key, Val oldValue, Val newValue){
		return this.localCache.asMap().replace(key, oldValue, newValue);
	}

	/**
	 * Is present key
	 * @param key
	 * @return
	 */
	public boolean containKey(Key key) {
		return this.localCache.asMap().containsKey(key);
	}
	/**
	 * 对某个key 失效
	 * @param key
	 */
	public void invalidate(Key key) {
		this.localCache.invalidate(key);
	}
	/**
	 * 对所有key 失效
	 */
	public void invalidateAll() {
		this.localCache.invalidateAll();
	}
	/**
	 * 对指定key list 失效
	 */
	public void invalidateAll(List<Key> keys) {
		this.localCache.invalidateAll(keys);
	}
	/**
	 * 对某个key 数组 失效
	 */
	public void invalidateAll(Key ... keys) {
		invalidateAll(Arrays.asList(keys));
	}

	/***
	 * size
	 * @return
	 */
	public long size() {
		return this.localCache.size();
	}

	/***
	 * 各种统计数据
	 * @return
	 */
	public CacheStats stats(){
		return this.localCache.stats();
	}
}
