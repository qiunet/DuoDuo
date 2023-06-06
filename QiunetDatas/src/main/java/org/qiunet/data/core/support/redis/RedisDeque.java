package org.qiunet.data.core.support.redis;

import com.google.common.base.Preconditions;
import redis.clients.jedis.Transaction;

import java.util.Deque;
import java.util.Iterator;

/***
 * redis 队列封装
 * @author qiunet
 * 2023/5/16 16:42
 */
public class RedisDeque extends RedisCollection implements Deque<String> {

	public RedisDeque(IRedisUtil redisUtil, String key) {
		super(redisUtil, key);
	}
	@Override
	public boolean offerFirst(String e) {
		Preconditions.checkNotNull(e);
		return redisUtil.returnJedis().lpush(key, e) > 0;
	}

	@Override
	public boolean offerLast(String e) {
		Preconditions.checkNotNull(e);
		return redisUtil.returnJedis().rpush(key, e) > 0;
	}

	@Override
	public String removeFirst() {
		return redisUtil.returnJedis().lpop(key);
	}

	@Override
	public String removeLast() {
		return redisUtil.returnJedis().rpop(key);
	}

	@Override
	public String getFirst() {
		return redisUtil.returnJedis().lindex(key, 0);
	}

	@Override
	public String getLast() {
		return redisUtil.returnJedis().lindex(key, -1);
	}

	@Override
	public void addFirst(String e) {
		offerFirst(e);
	}

	@Override
	public void addLast(String e) {
		offerLast(e);
	}

	@Override
	public String pollFirst() {
		return removeFirst();
	}

	@Override
	public String pollLast() {
		return removeLast();
	}

	@Override
	public String peekFirst() {
		return getFirst();
	}

	@Override
	public String peekLast() {
		return getLast();
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		Preconditions.checkNotNull(o);
		return redisUtil.returnJedis().lrem(key, 1, o.toString()) > 0;
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		Preconditions.checkNotNull(o);
		redisUtil.execCommands(jedis -> {
			int index = jedis.lrange(key, 0, -1).lastIndexOf(o);
			try (Transaction transaction = jedis.multi()) {
				transaction.lset(key, index, "__delete__");
				transaction.lrem(key, 1, "__delete__");
				transaction.exec();
			}
			return null;
		});
		return true;
	}

	@Override
	public boolean offer(String e) {
		return offerLast(e);
	}

	@Override
	public String remove() {
		return pollFirst();
	}

	@Override
	public String poll() {
		return pollLast();
	}

	@Override
	public String element() {
		return getFirst();
	}

	@Override
	public String peek() {
		return getFirst();
	}

	@Override
	public void push(String e) {
		this.addFirst(e);
	}

	@Override
	public String pop() {
		return pollFirst();
	}

	@Override
	public Iterator<String> descendingIterator() {
		return iterator();
	}
}
