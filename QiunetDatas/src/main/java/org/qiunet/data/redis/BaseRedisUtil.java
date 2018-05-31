package org.qiunet.data.redis;

import org.qiunet.data.redis.base.MoreKeyRedisCommand;
import org.qiunet.data.redis.base.RedisCommand;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.sql.SQLException;
import java.util.*;

abstract class BaseRedisUtil {
	protected final String PLACEHOLDER = "PLACEHOLDER";
	/***缓存一天*/
	protected final int NORMAL_LIFECYCLE=86400;
	/** 数据源 */
	protected JedisPool jedisPool;

	protected String redisName;
	/***
	 * 构造redisUtil 需要的JedisPool
	 * @param redisProperties
	 * @param redisName
	 */
	protected BaseRedisUtil(IKeyValueData<Object, Object> redisProperties, String redisName) {
		this.redisName = redisName;

		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(redisProperties.getInt(getConfigKey("maxIdle"), 30));
		poolConfig.setMaxTotal(redisProperties.getInt(getConfigKey("maxTotal"), 100));
		poolConfig.setTestWhileIdle(redisProperties.getBoolean(getConfigKey("testWhileIdle")));
		poolConfig.setMaxWaitMillis(redisProperties.getInt(getConfigKey("maxWaitMillis"), 3000));
		poolConfig.setNumTestsPerEvictionRun(redisProperties.getInt(getConfigKey("numTestsPerEvictionRun"), 30));
		poolConfig.setMinEvictableIdleTimeMillis(redisProperties.getInt(getConfigKey("minEvictableIdleTimeMillis"), 60000));
		poolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getInt(getConfigKey("timeBetweenEvictionRunsMillis"), 60000));

		String host = redisProperties.getString(getConfigKey("host"));
		int port = redisProperties.getInt(getConfigKey("port"));
		String password = redisProperties.getString(getConfigKey("pass"));
		int timeout = redisProperties.getInt(getConfigKey("timeout"), 2000);
		if (!StringUtil.isEmpty(password)) {
			this.jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
		}else {
			this.jedisPool = new JedisPool(poolConfig, host, port, timeout);
		}

		ShutdownHookThread.getInstance().addShutdownHook(() -> {
			// 添加关闭.
			this.jedisPool.close();
		});
	}

	protected BaseRedisUtil(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	private String getConfigKey(String originConfigKey) {
		// 返回类似: redis.{redisName}.host 的字符串
		return "redis."+redisName+"."+originConfigKey;
	}

	/**
	 * hlen  hash的长度
	 * @param key redis的key
	 * @return 返回jedis 返回的值
	 */
	public int hlen(String key){
		return new RedisCommand<Integer>(jedisPool, key) {
			@Override
			protected Integer expression(Jedis jedis, String key)  throws Exception{
				Long ret = jedis.hlen(key);
				return ret == null ? 0 : ret.intValue();
			}
		}.execAndReturn();
	}
	public String hget(String key,final String subKey){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) {
				return jedis.hget(key, subKey);
			}
			@Override
			protected Object[] params() {
				return new Object[]{subKey};
			}
		}.execAndReturn();
	}

	public long ttl(String key){
		return new RedisCommand<Long>(jedisPool, key) {
			@Override
			protected Long expression(Jedis jedis, String key) {
				return jedis.ttl(key);
			}
			@Override
			protected Object[] params() {
				return new Object[]{};
			}
		}.execAndReturn();
	}

	public Long lpush(String key, final String val){
		return new RedisCommand<Long>(jedisPool, key) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.lpush(key, val);
			}
			@Override
			protected Object[] params() {
				return new Object[]{val};
			}
		}.execAndReturn();
	}

	public List<String> lrange(String key, final int start, final int end){
		return new RedisCommand<List<String>>(jedisPool, key, Collections.<String>emptyList()) {
			@Override
			protected List<String> expression(Jedis jedis, String key) throws Exception {
				return jedis.lrange(key, start, end);
			}
			@Override
			protected Object[] params() {
				return new Object[]{start, end};
			}
		}.execAndReturn();
	}

	public void lset(String key,final long index,final String val){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				jedis.lset(key, index, val);
				return null;
			}

			@Override
			protected Object[] params() {
				return new Object[]{index, val};
			}
		}.execAndReturn();
	}

	public void ltrim(String key,final long start,final long end){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				jedis.ltrim(key, start, end);
				return null;
			}

			@Override
			protected Object[] params() {
				return new Object[]{start, end};
			}
		}.execAndReturn();
	}

	public long hdel(String key, final String subKey){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.hdel(key, subKey);
			}
			@Override
			protected Object[] params() {
				return new Object[]{subKey};
			}
		}.execAndReturn();
	}

	/**
	 * @param keys redis的keys
	 * @return 返回jedis 返回的值
	 */
	public List<String> mget(final List<String> keys){
		if(keys == null || keys.isEmpty()){
			return Collections.emptyList();
		}

		return new MoreKeyRedisCommand<List<String>>(jedisPool, Collections.<String>emptyList()) {
			@Override
			protected List<String> expression(Jedis jedis) throws Exception {
				List<String> rt=new ArrayList<String>();
				int maxKey=50;
				if(keys.size()>maxKey){
					List<String> tmp=new ArrayList<String>();
					int size = keys.size();
					int page = size / maxKey + ((size % maxKey)>0?1:0);
					for(int i=0;i<page;i++){
						tmp.addAll(CommonUtil.getSubListPage(keys, i*maxKey, maxKey));
						rt.addAll(jedis.mget(tmp.toArray(new String[0])));
						tmp.clear();
					}
				}else{
					String[] keys2 = keys.toArray(new String[0]);
					rt = jedis.mget(keys2);
				}
				return rt;
			}
		}.execAndReturn();
	}
	/**
	 * 存储一段data数组到redis
	 * @param key redis的key
	 * @param data 数据
	 * @param seconds redis 对象的存活时间(秒)
	 */
	public void setByteArrays(String key,final byte [] data, final int seconds){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				jedis.setex(key.getBytes(), seconds, data);
				return null;
			}
		}.execAndReturn();
	}
	/**
	 * 返回key所关联的byte []
	 * @param key redis的key
	 * @return 返回jedis 返回的值
	 */
	public byte[] getByteArray(String key){
		return new RedisCommand<byte[]>(jedisPool, key) {
			@Override
			protected byte[] expression(Jedis jedis, String key) throws Exception {
				return jedis.get(key.getBytes());
			}
		}.execAndReturn();
	}

	/**
	 * 向set中插入
	 * @param key redis的key
	 * @param values 值
	 * @return 返回jedis 返回的值
	 */
	public long saddString(String key,final String... values){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.sadd(key,values);
				return ret == null ? 0 : ret.longValue();
			}
			@Override
			protected Object[] params() {
				return values;
			}
		}.execAndReturn();
	}
	/**
	 * 向set中插入
	 * @param key redis的key
	 * @param value 值
	 * @param second 存活时间(秒)
	 * @return 返回jedis 返回的值
	 */
	public long saddByExpireSecond(String key,final String value,final int second){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.sadd(key,value);
				jedis.expire(key, second);
				return ret == null ? 0 : ret.longValue();
			}

			@Override
			protected Object[] params() {
				return new Object[]{value, second};
			}
		}.execAndReturn();
	}
	/**
	 * 获得集合里面的随机 1个元素
	 * @param key redis的key
	 * @return 返回jedis 返回的值
	 */
	public String srandmember(String key){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.srandmember(key);
			}
		}.execAndReturn();
	}

	/**
	 * 返回集合key的基数(集合中元素的数量)。
	 * @param key redis的key
	 * @return 返回jedis 返回的值
	 */
	public long scardString(String key){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.scard(key);
			}
		}.execAndReturn();
	}

	public String spopString(String key){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.spop(key);
			}
		}.execAndReturn();
	}

	public long del(String key){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.del(key);
				return ret == null ? 0 : ret.longValue();
			}
		}.execAndReturn();
	}

	public Map<String,String> hmgetAllString(String key){
		return new RedisCommand<Map<String, String>>(jedisPool, key) {
			@Override
			protected Map<String, String> expression(Jedis jedis, String key) throws Exception {
				return jedis.hgetAll(key);
			}
		}.execAndReturn();
	}

	public String hmsetAllString(String key, final Map<String,String> values){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.hmset(key, values);
			}
		}.execAndReturn();
	}

	public void hset(String key,final String field,final String value){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				jedis.hset(key,field,value);
				return null;
			}
			@Override
			protected Object[] params() {
				return new Object[]{field, value};
			}
		}.execAndReturn();
	}

	/***
	 * 对hash 的字段自增 减
	 * @param key
	 * @param field
	 * @param value
	 */
	public Long hincr(String key,final String field,final long value){
		return new RedisCommand<Long>(jedisPool, key) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.hincrBy(key, field, value);
				return ret;
			}
			@Override
			protected Object[] params() {
				return new Object[]{field, value};
			}
		}.execAndReturn();
	}

	public void hsetString(String key,final String field,final String value){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				jedis.hset(key,field,value);
				jedis.expire(key, NORMAL_LIFECYCLE);
				return null;
			}
			@Override
			protected Object[] params() {
				return new Object[]{field, value};
			}
		}.execAndReturn();
	}


	public void setString(String key,final String value,final int seconds) {
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				jedis.set(key, value);
				if(seconds > 0){
					jedis.expire(key, seconds);
				}
				return null;
			}

			@Override
			protected Object[] params() {
				return new Object[]{value, seconds};
			}
		}.execAndReturn();
	}

	public void persist(String key){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				jedis.persist(key);
				return null;
			}
		}.execAndReturn();
	}

	public String getString(String key){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.get(key);
			}
		}.execAndReturn();
	}

	public String getString(String key, final int lifecycle){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				String rt = jedis.get(key);
				if(lifecycle > 0){
					jedis.expire(key, lifecycle);
				}
				return rt;
			}
			@Override
			protected Object[] params() {
				return new Object[]{lifecycle};
			}
		}.execAndReturn();
	}

	public void expire(String key,final int seconds){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				jedis.expire(key, seconds);
				return null;
			}
			@Override
			protected Object[] params() {
				return new Object[]{seconds};
			}
		}.execAndReturn();
	}

	public Set<String> zRevRangeByScore(String key, final double max, final double min, final int offset, final int count){
		return new RedisCommand<Set<String>>(jedisPool, key, Collections.<String>emptySet()) {
			@Override
			protected Set<String> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrevrangeByScore(key, max, min, offset, count);
			}

			@Override
			protected Object[] params() {
				return new Object[]{max, min, offset, count};
			}
		}.execAndReturn();
	}

	public Set<Tuple> zRevRangeByScoreWithScores(String key, final double max, final double min, final int offset, final int count){
		return new RedisCommand<Set<Tuple>>(jedisPool, key, Collections.<Tuple>emptySet()) {
			@Override
			protected Set<Tuple> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
			}
			@Override
			protected Object[] params() {
				return new Object[]{max, min, offset, count};
			}
		}.execAndReturn();
	}

	public Set<Tuple> zRangeByScoreWithScores(String key, final double max,final double min,final int offset,final int count){
		return new RedisCommand<Set<Tuple>>(jedisPool, key, Collections.<Tuple>emptySet()) {
			@Override
			protected Set<Tuple> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrangeByScoreWithScores(key, max, min, offset, count);
			}
			@Override
			protected Object[] params() {
				return new Object[]{max, min, offset, count};
			}
		}.execAndReturn();
	}

	public Set<Tuple> zRangeByScoreWithScores(String key,final double max,final double min){
		return new RedisCommand<Set<Tuple>>(jedisPool, key, Collections.<Tuple>emptySet()) {
			@Override
			protected Set<Tuple> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrangeByScoreWithScores(key, min, max);
			}
			@Override
			protected Object[] params() {
				return new Object[]{max, min };
			}
		}.execAndReturn();
	}

	public Set<Tuple> zRevRangeWithScores(String key,final long start,final long end){
		return new RedisCommand<Set<Tuple>>(jedisPool, key, Collections.<Tuple>emptySet()) {
			@Override
			protected Set<Tuple> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrevrangeWithScores(key, start, end);
			}

			@Override
			protected Object[] params() {
				return new Object[]{start, end };
			}
		}.execAndReturn();
	}

	public Set<String> zRevrange(String key,final long start,final long end){
		return new RedisCommand<Set<String>>(jedisPool, key) {
			@Override
			protected Set<String> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrevrange(key, start, end);
			}

			@Override
			protected Object[] params() {
				return new Object[]{start, end };
			}
		}.execAndReturn();
	}

	public Set<String> zRange(String key, final long start,final long end){
		return new RedisCommand<Set<String>>(jedisPool, key) {
			@Override
			protected Set<String> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrange(key, start, end);
			}

			@Override
			protected Object[] params() {
				return new Object[]{start, end };
			}
		}.execAndReturn();
	}

	public Long zRevRank(String key,final String member){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zrevrank(key, member);
				return ret == null ? 0 : ret;
			}
			@Override
			protected Object[] params() {
				return new Object[]{member};
			}
		}.execAndReturn();
	}


	public Long zrank(String key,final String member){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zrank(key, member);
				return ret;
			}
			@Override
			protected Object[] params() {
				return new Object[]{member};
			}
		}.execAndReturn();
	}


	public double zscore(String key,final String member){
		return new RedisCommand<Double>(jedisPool, key, 0d) {
			@Override
			protected Double expression(Jedis jedis, String key) throws Exception {
				Double ret = jedis.zscore(key, member);
				return ret == null ? 0 : ret;
			}
			@Override
			protected Object[] params() {
				return new Object[]{member};
			}
		}.execAndReturn();
	}
	/**
	 * 使zset 某个member增长某个数
	 * @param key redis的key
	 * @param member 成员
	 * @param val 值
	 * @return 返回jedis 返回的值
	 */
	public double zincrby(String key, final String member,final double val){
		return new RedisCommand<Double>(jedisPool, key, 0d) {
			@Override
			protected Double expression(Jedis jedis, String key) throws Exception {
				Double ret = jedis.zincrby(key, val, member);
				return ret == null ? 0 : ret;
			}
			@Override
			protected Object[] params() {
				return new Object[]{member , val};
			}
		}.execAndReturn();
	}

	public long zAdd(String key, double score, String member){
		return zAdd(key, score, member, -1);
	}
	public long zAdd(String key,final double score,final String member,final int expire){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zadd(key, score, member);
				if(expire  >= 0){
					jedis.expire(key, expire);
				}
				return ret == null ? 0 : ret;
			}

			@Override
			protected Object[] params() {
				return new Object[]{score , member };
			}
		}.execAndReturn();
	}

	public long zAdd(String key,final Map<String, Double> vals){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zadd(key, vals );
				return ret == null ? 0 : ret;
			}

			@Override
			protected Object[] params() {
				return new Object[]{JsonUtil.toJsonString(vals) };
			}
		}.execAndReturn();
	}

	public long zRem(String key,final String... members){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zrem(key, members);
				return ret == null ? 0 : ret;
			}

			@Override
			protected Object[] params() {
				return members;
			}
		}.execAndReturn();
	}

	public long zCount(String key,final double min,final double max){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zcount(key, min, max);
				return ret == null ? 0 : ret;
			}

			@Override
			protected Object[] params() {
				return new Object[]{min , max};
			}
		}.execAndReturn();
	}

	public long zCard(String key){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zcard(key);
				return ret == null ? 0 : ret;
			}
		}.execAndReturn();
	}

	public Set<String> zRevrangeByScore(String key,final double max,final double min){
		return new RedisCommand<Set<String>>(jedisPool, key, Collections.<String>emptySet()) {
			@Override
			protected Set<String> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrevrangeByScore(key, max, min);
			}
			@Override
			protected Object[] params() {
				return new Object[]{ max ,min};
			}
		}.execAndReturn();
	}

	/***
	 * 根据分数范围 删除zset里面的一批key
	 * @param key
	 * @param start
	 * @param end
	 */
	public Long zRemRangeByScore(String key,final double start,final double end){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.zremrangeByScore(key, start, end);
			}
			@Override
			protected Object[] params() {
				return new Object[]{ start ,end};
			}
		}.execAndReturn();
	}

	public boolean sismember(String key,final String member){
		return new RedisCommand<Boolean>(jedisPool, key, false) {
			@Override
			protected Boolean expression(Jedis jedis, String key) throws Exception {
				return jedis.sismember(key, member);
			}
			@Override
			protected Object[] params() {
				return new Object[]{ member};
			}
		}.execAndReturn();
	}

	public Set<String> smembers(String key){
		return new RedisCommand<Set<String>>(jedisPool, key) {
			@Override
			protected Set<String> expression(Jedis jedis, String key) throws Exception {
				return jedis.smembers(key);
			}
			@Override
			protected Object[] params() {
				return new Object[]{};
			}
		}.execAndReturn();
	}

	public long srem(String key,final String member){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.srem(key, member);
			}
			@Override
			protected Object[] params() {
				return new Object[]{ member};
			}
		}.execAndReturn();
	}

	public long incr(String key){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.incr(key);
			}
		}.execAndReturn();
	}

	public long incrby(String key, final int val){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.incrBy(key, val);
			}
			@Override
			protected Object[] params() {
				return new Object[]{ val};
			}
		}.execAndReturn();
	}

	public long decr(String key){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.decr(key);
			}
			@Override
			protected Object[] params() {
				return new Object[]{};
			}
		}.execAndReturn();
	}

	public long decrby(String key, final int val){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.decrBy(key, val);
			}
			@Override
			protected Object[] params() {
				return new Object[]{ val};
			}
		}.execAndReturn();
	}

	public String lpop(String key){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.lpop(key);
			}
		}.execAndReturn();
	}

	public String rpop(String key){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.rpop(key);
			}
		}.execAndReturn();
	}

	public long rpush(String key, String value){
		return rpush(key, value, -1);
	}
	public long rpush(String key, final String value,final int expire){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				long rt = jedis.rpush(key, value);
				if(expire>=0){
					jedis.expire(key, expire);
				}
				return rt;
			}
			@Override
			protected Object[] params() {
				return new Object[]{ value , expire};
			}
		}.execAndReturn();
	}

	public long rpush(String key,final String... values){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.rpush(key, values);
			}
			@Override
			protected Object[] params() {
				return values;
			}
		}.execAndReturn();
	}

	public long llen(String key){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return  jedis.llen(key);
			}
		}.execAndReturn();
	}


	public long lrem(String key, final long count ,final String val){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.lrem(key, count, val);
			}
		}.execAndReturn();
	}

	/**
	 * 判断有没有key-value  若有返回true
	 * @param key redis的key
	 * @param value 值
	 * @return 返回jedis 返回的值
	 */
	public long setnx(String key,final String value){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				return jedis.setnx(key, value);
			}
			@Override
			protected Object[] params() {
				return new Object[]{value};
			}
		}.execAndReturn();
	}
	/**
	 * 查看该key是否存在
	 * @param key redis的key
	 * @return 返回jedis 返回的值
	 */
	public boolean exists(String key){
		return new RedisCommand<Boolean>(jedisPool, key, false) {
			@Override
			protected Boolean expression(Jedis jedis, String key) throws Exception {
				return jedis.exists(key);
			}
		}.execAndReturn();
	}


}
