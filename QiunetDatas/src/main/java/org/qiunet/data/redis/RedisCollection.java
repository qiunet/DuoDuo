package org.qiunet.data.redis;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/***
 * redis 集合
 *
 * @author qiunet
 * 2023/5/16 17:17
 */
public class RedisCollection implements Collection<String> {
	/**
	 * 使用的redis 实例
	 */
	protected final IRedisUtil redisUtil;
	/**
	 * redis key
	 */
	protected final String key;

	public RedisCollection(IRedisUtil redisUtil, String key) {
		this.redisUtil = redisUtil;
		this.key = key;
	}

	/**
	 * =所有数据
	 * @return 列表
	 */
	protected List<String> getCollectionData() {
		return redisUtil.returnJedis().lrange(key, 0, -1);
	}

	@Override
	public int size() {
		Long llen = redisUtil.returnJedis().llen(key);
		return llen == null ? 0 : llen.intValue();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		return getCollectionData().contains(o);
	}

	@Override
	public Iterator<String> iterator() {
		return getCollectionData().iterator();
	}

	@Override
	public Object[] toArray() {
		return getCollectionData().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return getCollectionData().toArray(a);
	}

	@Override
	public boolean add(String s) {
		return redisUtil.returnJedis().rpush(key, s) > 0;
	}

	@Override
	public boolean remove(Object o) {
		return redisUtil.returnJedis().lrem(key, 1, String.valueOf(o)) == 1;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return getCollectionData().removeAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {
		return redisUtil.returnJedis().rpush(key, c.toArray(new String[0])) > 0;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean ok = true;
		List<String> temp = (List<String>)c;
		for (String value : temp) {
			if (redisUtil.returnJedis().lrem(key, 1, value) < 1) {
				ok = false;
			}
		}
		return ok;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean ok = true;
		List<String> list = getCollectionData();
		list.removeAll(c);
		for (String value : list) {
			if (redisUtil.returnJedis().lrem(key, 1, value) < 1) {
				ok = false;
			}
		}
		return ok;
	}

	@Override
	public void clear() {
		redisUtil.returnJedis().del(key);
	}
}
