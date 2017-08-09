package org.qiunet.data.redis;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Level;
import org.qiunet.data.redis.base.MoreKeyRedisCommand;
import org.qiunet.data.redis.base.RedisCommand;
import org.qiunet.data.redis.support.info.IRedisList;
import org.qiunet.data.redis.support.info.IRedisEntity;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.data.StringData;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 缓存的管理
 * @author qiunet
 *         Created on 16/12/28 08:28.
 */
public abstract class AbstractRedisUtil {
	//protected final Logger logger=Logger.getLogger(this.getClass());

	protected final String PLACEHOLDER = "PLACEHOLDER";
	/***缓存一天*/
	public  final int NORMAL_LIFECYCLE=86400;
	/** 数据源 */
	protected JedisPool jedisPool;
	/**
	 * @param jedisPool the jedisPool to set
	 */
	public AbstractRedisUtil(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	/**
	 * 将对象保存到hash中,并且设置默认生命周期
	 * @param key redis的key
	 * @param po redisEntity对象
	 */
	public void setObjectToHash(String key,IRedisEntity po){
		setObjectToHash(key, po, NORMAL_LIFECYCLE);
	}
	/**
	 * 将对象保存到hash中,并且设置生命周期
	 * @param key redis的key
	 * @param po redisEntity对象
	 * @param seconds redis 对象的存活时间(秒)
	 */
	public void setObjectToHash(String key, final IRedisEntity po, final int seconds){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				Map<String, String> map = po.getAllFeildsToHash();
				jedis.hmset(key, map);
				jedis.expire(key, seconds);
				return null;
			}
			@Override
			protected String cmdName() {
				return "setObjectToHash";
			}

			@Override
			protected Object[] params() {
				return new Object[]{StringData.parseString(po), seconds};
			}
		}.execAndReturn();
	}
	/**
	 * 将对象保存到hash中,并且设置生命周期
	 * @param key redis的key
	 * @param po redisEntity对象
	 * @param fields 需要保存的字段
	 */
	public void setObjectToHash(String key, final IRedisEntity po, final String ...fields){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				Map<String, String> map = CommonUtil.getMap(po, fields);
				jedis.hmset(key, map);
				jedis.expire(key, NORMAL_LIFECYCLE);
				return null;
			}
			@Override
			protected String cmdName() {
				return "setObjectToHash";
			}

			@Override
			protected Object[] params() {
				return fields;
			}
		}.execAndReturn();
	}
	/**
	 * 通过反射从缓存里获取一个对象 缺省默认时间，默认的key是有uid这个字段拼接而成
	 * @param key redis的key
	 * @param clazz 泛型的对象的class
	 * @param <T> 泛型对象
	 * @return 返回jedis 返回的值 对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObjectFromHash(String key,Class<? extends IRedisEntity> clazz){
		return (T)getObjectFromHash(key, clazz, NORMAL_LIFECYCLE);
	}
	/**
	 * 通过反射从缓存里获取一个对象
	 * @param key redis的key
	 * @param clazz 泛型的对象的class
	 * @param seconds redis 对象的存活时间(秒) redis 对象的时间
	 * @return 返回jedis 返回的值
	 */
	@SuppressWarnings("unchecked")
	private <T extends IRedisEntity> T getObjectFromHash(String key, final Class<T> clazz, final int seconds){
		return new RedisCommand<T>(jedisPool, key) {
			@Override
			protected T expression(Jedis jedis, String key)  throws Exception{
				Map<String, String> map=jedis.hgetAll(key);
				T obj = clazz.newInstance();
				if(! map.isEmpty()){
					if(! (obj instanceof IRedisList) && map.size() != obj.getFieldCount()) {
						Field fields = clazz.getDeclaredField("fields");
						if (fields != null) {
							fields.setAccessible(true);
							String [] fieldStrs = (String[]) fields.get(obj);
							logger.error("ObjFieldSizeError getObjectFromHash:"+ clazz.getSimpleName() +" Map:"+JSON.toJSONString(map) +" ObjFields"+JsonUtil.toJsonString(fieldStrs));
						}else {
							logger.error("ObjFieldSizeError getObjectFromHash:"+ clazz.getSimpleName() +" Map:"+JSON.toJSONString(map) +" ObjFieldCount:"+obj.getFieldCount());
						}
						jedis.expire(key, 0);
						return null;
					}
					map.put(obj.getDbInfoKeyName(), StringUtil.split(key,"#")[1]);
					jedis.expire(key, seconds);
					return (T)CommonUtil.getObjFromMap(map, obj);
				}else{
					jedis.expire(key, 0);
				}
				return null;
			}
			@Override
			protected String cmdName() {
				return "getObjectFromHash";
			}

			@Override
			protected Object[] params() {
				return new Object[]{clazz.getSimpleName(), seconds};
			}
		}.execAndReturn();
	}
	/**
	 * 将一个列表对象放入缓存
	 * @param key redis的key
	 * @param list IRedisList list
	 */
	public void setListToHash(String key,List<? extends IRedisList> list){
		setListToHash(key, list, NORMAL_LIFECYCLE);
	}
	/**
	 * 将一个列表对象放入缓存，并设置有效期
	 * @param key redis的key
	 * @param list IRedisList list
	 * @param seconds redis 对象的存活时间(秒) redis 对象的时间
	 */
	private void setListToHash(String key, final List<? extends IRedisList> list,final int seconds){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key)  throws Exception{
				Map<String,String> map = new HashMap<String, String>();
				Map<String,String> keyMap = null;
				String keyName=null;
				for(IRedisList po : list){
					keyName=po.getSubKey();
					keyMap=CommonUtil.getMap(po, keyName);

					map.put(String.valueOf(keyMap.get(keyName)), JsonUtil.toJsonString(po.getAllFeildsToHash()));
				}
				map.put(PLACEHOLDER, "");

				jedis.hmset(key, map);
				jedis.expire(key, seconds);
				return null;
			}

			@Override
			protected String cmdName() {
				return "setListToHash";
			}

			@Override
			protected Object[] params() {
				return new Object[]{StringData.parseString(list) , seconds};
			}
		}.execAndReturn();
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
				return ret == null ? -1 : ret.intValue();
			}

			@Override
			protected String cmdName() {
				return "hlen";
			}
		}.execAndReturn();
	}

	/**
	 * 从缓存里还原一个列表对象
	 * @param key redis的key
	 * @param clazz 泛型的对象的class
	 * @param <T> 泛型对象
	 * @return 返回jedis 返回的值
	 */
	public <T extends IRedisList> List<T> getListFromHash(String key,Class<T> clazz) {
		return getListFromHash(key, clazz, NORMAL_LIFECYCLE);
	}
	/**
	 * 从缓存里还原一个列表对象
	 * @param key redis的key
	 * @param clazz 泛型的对象的class
	 * @param seconds redis 对象的存活时间(秒) redis 对象的时间
	 * @param <T> 泛型对象
	 * @return 返回jedis 返回的值
	 */
	@SuppressWarnings("unchecked")
	public <T extends IRedisList> List<T> getListFromHash(String key, final  Class<T> clazz,final int seconds){
		return new RedisCommand<List<T>>(jedisPool, key) {
			@Override
			protected List<T> expression(Jedis jedis, String key) throws Exception {
				Map<String,String> map = jedis.hgetAll(key);

				if(map != null && ! map.isEmpty()){
					map.remove(PLACEHOLDER);
					List<T> rt=new ArrayList();
					for(Map.Entry<String, String> entry:map.entrySet()){
						T po = clazz.newInstance();
						String fieldKey = entry.getKey();
						Map<String,String> mapFields = JsonUtil.getGeneralObject(entry.getValue(), Map.class);
						if (mapFields.size() != po.getFieldCount()) {
							Field fields = clazz.getDeclaredField("fields");
							if (fields != null) {
								fields.setAccessible(true);
								String[] fieldStrs = (String[]) fields.get(po);
								logger.error("ListFieldSizeError getListFromHash ["+clazz.getSimpleName()+"]! Map["+entry.getValue()+"] ObjFields "+JsonUtil.toJsonString(fieldStrs));
							}else {
								logger.error("ListFieldSizeError getListFromHash ["+clazz.getSimpleName()+"]! Map["+entry.getValue()+"]");
							}
							jedis.expire(key, 0);
							return null;
						}

						mapFields.put(po.getDbInfoKeyName(), StringUtil.split(key,"#")[1]);
						mapFields.put(po.getSubKey() , fieldKey);
						CommonUtil.getObjFromMap(mapFields, po);
						rt.add(po);
					}
					jedis.expire(clazz.getSimpleName(), seconds);
					return rt;
				}
				return null;
			}

			@Override
			protected String cmdName() {
				return "getListFromHash";
			}

			@Override
			protected Object[] params() {
				return new Object[]{clazz.getSimpleName() , seconds};
			}
		}.execAndReturn();
	}
	/***
	 *
	 * @param key redis的key
	 * @param clazz 泛型的对象的class
	 * @param subKey subid
	 * @param <T> 泛型对象
	 * @return 返回jedis 返回的值
	 */
	public <T extends IRedisList> T getRedisObjectFromRedisList(String key,final Class<T> clazz ,final String subKey){
		return new RedisCommand<T>(jedisPool, key) {
			@Override
			protected T expression(Jedis jedis, String key) throws Exception {
				T po=null;
				String val = jedis.hget(key, subKey);
				if(!StringUtil.isEmpty(val)){
					po = (T) clazz.newInstance();
					Map<String, String> mapFields = JsonUtil.getGeneralObject(val, Map.class);
					mapFields.put(po.getDbInfoKeyName(), StringUtil.split(key,"#")[1]);
					mapFields.put( po.getSubKey() , subKey);
					CommonUtil.getObjFromMap(mapFields, po);
					jedis.expire(key, NORMAL_LIFECYCLE);
				}
				return po;
			}

			@Override
			protected String cmdName() {
				return "getRedisObjectFromRedisList";
			}

			@Override
			protected Object[] params() {
				return new Object[]{clazz.getSimpleName(), subKey};
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
			protected String cmdName() {
				return "hget";
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
			protected String cmdName() {
				return "hget";
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
			protected String cmdName() {
				return "lpush";
			}
			@Override
			protected Object[] params() {
				return new Object[]{val};
			}
		}.execAndReturn();
	}

	public List<String> lrange(String key, final int start,final int end){
		return new RedisCommand<List<String>>(jedisPool, key, Collections.<String>emptyList()) {
			@Override
			protected List<String> expression(Jedis jedis, String key) throws Exception {
				return jedis.lrange(key, start, end);
			}

			@Override
			protected String cmdName() {
				return "lrange";
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
			protected String cmdName() {
				return "lset";
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
			protected String cmdName() {
				return "ltrim";
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
			protected String cmdName() {
				return "hdel";
			}

			@Override
			protected Object[] params() {
				return new Object[]{subKey};
			}
		}.execAndReturn();
	}

	/**
	 * 批量删除对象
	 * @param key redis的key
	 * @param list IRedisList list
	 */
	public void deleteList(String key,final List<? extends IRedisList> list){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				String keys[]=new String[list.size()];
				String keyNames[]=null;
				int index=0;
				for(IRedisList po:list){
					Map<String,String> keyMap=CommonUtil.getMap(po, po.getSubKey());
					keys[index++]=keyMap.get(po.getSubKey()).toString();
				}
				jedis.hdel(key, keys);
				return null;
			}

			@Override
			protected String cmdName() {
				return "deleteList";
			}

			@Override
			protected Object[] params() {
				return new Object[]{list};
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
			protected String cmdName() {
				return "mget";
			}
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

			@Override
			protected String cmdName() {
				return "setByteArrays";
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

			@Override
			protected String cmdName() {
				return "getByteArray";
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
		return new RedisCommand<Long>(jedisPool, key, -1L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.sadd(key,values);
				return ret == null ? -1 : ret.longValue();
			}

			@Override
			protected String cmdName() {
				return "saddString";
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
		return new RedisCommand<Long>(jedisPool, key, -1L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.sadd(key,value);
				jedis.expire(key, second);
				return ret == null ? -1 : ret.longValue();
			}

			@Override
			protected String cmdName() {
				return "saddByExpireSecond";
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

			@Override
			protected String cmdName() {
				return "srandmember";
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

			@Override
			protected String cmdName() {
				return "scardString";
			}
		}.execAndReturn();
	}

	public String spopString(String key){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.spop(key);
			}
			@Override
			protected String cmdName() {
				return "spopString";
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
			@Override
			protected String cmdName() {
				return "del";
			}
		}.execAndReturn();
	}

	public Map<String,String> hmgetAllString(String key){
		return new RedisCommand<Map<String, String>>(jedisPool, key) {
			@Override
			protected Map<String, String> expression(Jedis jedis, String key) throws Exception {
				return jedis.hgetAll(key);
			}
			@Override
			protected String cmdName() {
				return "hmgetAllString";
			}

			@Override
			protected Level getLogLevel() {
				return Level.DEBUG;
			}
		}.execAndReturn();
	}

	public String hmsetAllString(String key, final Map<String,String> values){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.hmset(key, values);
			}

			@Override
			protected String cmdName() {
				return "hmsetAllString";
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
			protected String cmdName() {
				return "hset";
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
			protected String cmdName() {
				return "hincr";
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
			protected String cmdName() {
				return "hsetString";
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
			protected String cmdName() {
				return "setString";
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

			@Override
			protected String cmdName() {
				return "persist";
			}
		}.execAndReturn();
	}

	public String getString(String key){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.get(key);
			}

			@Override
			protected String cmdName() {
				return "getString";
			}
		}.execAndReturn();
	}
	public String getString(String key, final int lifecycle){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				String rt = jedis.get(key);
				if(lifecycle>0){
					jedis.expire(key, lifecycle);
				}
				return rt;
			}

			@Override
			protected String cmdName() {
				return "getString";
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
			protected String cmdName() {
				return "expire";
			}

			@Override
			protected Object[] params() {
				return new Object[]{seconds};
			}
		}.execAndReturn();
	}

	public Set<String> zRevRangeByScore(String key, final double max,final double min,final int offset,final int count){
		return new RedisCommand<Set<String>>(jedisPool, key, Collections.<String>emptySet()) {
			@Override
			protected Set<String> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrevrangeByScore(key, max, min, offset, count);
			}

			@Override
			protected String cmdName() {
				return "zRevRangeByScore";
			}

			@Override
			protected Object[] params() {
				return new Object[]{max, min, offset, count};
			}
		}.execAndReturn();
	}

	public Set<Tuple> zRevRangeByScoreWithScores(String key, final double max,final double min,final int offset,final int count){
		return new RedisCommand<Set<Tuple>>(jedisPool, key, Collections.<Tuple>emptySet()) {
			@Override
			protected Set<Tuple> expression(Jedis jedis, String key) throws Exception {
				return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
			}

			@Override
			protected String cmdName() {
				return "zRevRangeByScoreWithScores";
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
			protected String cmdName() {
				return "zRangeByScoreWithScores";
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
			protected String cmdName() {
				return "zRangeByScoreWithScores";
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
			protected String cmdName() {
				return "zRevRangeWithScores";
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
			protected String cmdName() {
				return "zRevrange";
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
			protected String cmdName() {
				return "zRange";
			}
			@Override
			protected Object[] params() {
				return new Object[]{start, end };
			}
		}.execAndReturn();
	}

	public long zRevRank(String key,final String member){
		return new RedisCommand<Long>(jedisPool, key, -1L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zrevrank(key, member);
				return ret == null ? -1 : ret;
			}

			@Override
			protected String cmdName() {
				return "zRevRank";
			}
			@Override
			protected Object[] params() {
				return new Object[]{member};
			}
		}.execAndReturn();
	}

	public double zscore(String key,final String member){
		return new RedisCommand<Double>(jedisPool, key, -1d) {
			@Override
			protected Double expression(Jedis jedis, String key) throws Exception {
				Double ret = jedis.zscore(key, member);
				return ret == null ? -1 : ret;
			}

			@Override
			protected String cmdName() {
				return "zscore";
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
		return new RedisCommand<Double>(jedisPool, key, -1d) {
			@Override
			protected Double expression(Jedis jedis, String key) throws Exception {
				Double ret = jedis.zincrby(key, val, member);
				return ret == null ? -1 : ret;
			}

			@Override
			protected String cmdName() {
				return "zincrby";
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
		return new RedisCommand<Long>(jedisPool, key, -1L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zadd(key, score, member);
				if(expire>=0){
					jedis.expire(key, expire);
				}
				return ret == null ? -1 : ret;
			}

			@Override
			protected String cmdName() {
				return "zAdd";
			}
			@Override
			protected Object[] params() {
				return new Object[]{score , member };
			}
		}.execAndReturn();
	}

	public long zAdd(String key,final Map<String, Double> vals){
		return new RedisCommand<Long>(jedisPool, key, -1L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zadd(key, vals );
				return ret == null ? -1 : ret;
			}

			@Override
			protected String cmdName() {
				return "zAdd";
			}
			@Override
			protected Object[] params() {
				return new Object[]{JsonUtil.toJsonString(vals) };
			}
		}.execAndReturn();
	}

	public long zRem(String key,final String... members){
		return new RedisCommand<Long>(jedisPool, key, -1L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				Long ret = jedis.zrem(key, members);
				logger.info("jedis zRem key:" + key + " memebers:" + Arrays.toString(members) + " ret:" + ret);
				return ret == null ? -1 : ret;
			}

			@Override
			protected String cmdName() {
				return "zRem";
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
			protected String cmdName() {
				return "zCount";
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

			@Override
			protected String cmdName() {
				return "zCard";
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
			protected String cmdName() {
				return "zRevrangeByScore";
			}
			@Override
			protected Object[] params() {
				return new Object[]{ max ,min};
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
			protected String cmdName() {
				return "sismember";
			}
			@Override
			protected Object[] params() {
				return new Object[]{ member};
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
			protected String cmdName() {
				return "srem";
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

			@Override
			protected String cmdName() {
				return "incr";
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
			protected String cmdName() {
				return "incrby";
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

			@Override
			protected String cmdName() {
				return "lpop";
			}
		}.execAndReturn();
	}

	public String rpop(String key){
		return new RedisCommand<String>(jedisPool, key) {
			@Override
			protected String expression(Jedis jedis, String key) throws Exception {
				return jedis.rpop(key);
			}

			@Override
			protected String cmdName() {
				return "rpop";
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
			protected String cmdName() {
				return "rpush";
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
				return rpush(key, values);
			}

			@Override
			protected String cmdName() {
				return "rpush";
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

			@Override
			protected String cmdName() {
				return "llen";
			}
		}.execAndReturn();
	}

	/**
	 * 频繁请求访问控制
	 * @param key
	 * @return
	 */
	public long fastRequestControl(String key){
		return new RedisCommand<Long>(jedisPool, key, 0L) {
			@Override
			protected Long expression(Jedis jedis, String key) throws Exception {
				long ret = jedis.incr(key);
				jedis.expire(key, 3);
				return ret;
			}

			@Override
			protected String cmdName() {
				return "fastRequestControl";
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
				return setnx(key, value);
			}

			@Override
			protected String cmdName() {
				return "setnx";
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

			@Override
			protected String cmdName() {
				return "exists";
			}
		}.execAndReturn();
	}
}
