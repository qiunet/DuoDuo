package org.qiunet.data.redis;

import com.alibaba.fastjson.JSON;
import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.redis.base.MoreKeyRedisCommand;
import org.qiunet.data.redis.base.RedisCommand;
import org.qiunet.data.redis.support.info.IRedisList;
import org.qiunet.data.redis.support.info.IRedisEntity;
import org.qiunet.data.util.DataUtil;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.data.StringData;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 缓存的管理
 * @author qiunet
 *         Created on 16/12/28 08:28.
 */
public abstract class AbstractRedisUtil extends BaseRedisUtil {
	protected AbstractRedisUtil(IKeyValueData<Object, Object> redisProperties, String redisName) {
		super(redisProperties, redisName);
	}
	protected  AbstractRedisUtil(JedisPool jedisPool) {
		super(jedisPool);
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
	public void setObjectToHash(String key, final IRedisEntity po, final IField...fields){
		new RedisCommand<Object>(jedisPool, key) {
			@Override
			protected Object expression(Jedis jedis, String key) throws Exception {
				Map<String, String> map = DataUtil.getMap(po, fields);
				jedis.hmset(key, map);
				jedis.expire(key, NORMAL_LIFECYCLE);
				return null;
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

						IField [] fields = obj.getFields();
						logger.error("ObjFieldSizeError getObjectFromHash:"+ clazz.getSimpleName() +" Map:"+JSON.toJSONString(map) +" ObjFields"+JsonUtil.toJsonString(fields));
//						jedis.expire(key, 0);
//						return null;
					}
					map.put(obj.getDbInfoKeyName(), StringUtil.split(key,"#")[1]);
					jedis.expire(key, seconds);
					return DataUtil.getObjFromMap(map, obj);
				}else{
					jedis.expire(key, 0);
				}
				return null;
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
				String keyName = null;
				for(IRedisList po : list){
					map.put(String.valueOf(po.getSubId()), JsonUtil.toJsonString(po.getAllFeildsToHash()));
				}
				map.put(PLACEHOLDER, "");

				jedis.hmset(key, map);
				jedis.expire(key, seconds);
				return null;
			}
			@Override
			protected Object[] params() {
				return new Object[]{StringData.parseString(list) , seconds};
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
					List<T> rt = new ArrayList();
					for(Map.Entry<String, String> entry:map.entrySet()){
						T po = clazz.newInstance();
						String fieldKey = entry.getKey();
						Map<String,String> mapFields = JsonUtil.getGeneralObject(entry.getValue(), Map.class);
						if (mapFields.size() != po.getFieldCount()) {
							IField [] fields = po.getFields();
							logger.error("ListField Size Error getListFromHash ["+clazz.getSimpleName()+"]! Map["+entry.getValue()+"] ObjFields "+JsonUtil.toJsonString(fields));

//							jedis.expire(key, 0);
//							return null;
						}

						mapFields.put(po.getDbInfoKeyName(), StringUtil.split(key,"#")[1]);
						mapFields.put(po.getSubKey() , fieldKey);
						DataUtil.getObjFromMap(mapFields, po);
						rt.add(po);
					}
					jedis.expire(key, seconds);
					return rt;
				}
				return null;
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
					DataUtil.getObjFromMap(mapFields, po);
					jedis.expire(key, NORMAL_LIFECYCLE);
				}
				return po;
			}
			@Override
			protected Object[] params() {
				return new Object[]{clazz.getSimpleName(), subKey};
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
				String keys[] = new String[list.size()];
				int index=0;
				for(IRedisList po : list){
					keys[index++] = String.valueOf(po.getSubId());
				}
				jedis.hdel(key, keys);
				return null;
			}
			@Override
			protected Object[] params() {
				return new Object[]{list};
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

		return new MoreKeyRedisCommand<List<String>>(jedisPool, Collections.emptyList()) {
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
}
