package org.qiunet.data.core.support;

import org.qiunet.data.core.support.entityInfo.IEntityListInfo;
import org.qiunet.data.db.support.base.DbListSupport;
import org.qiunet.data.db.support.base.IDbList;
import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.support.info.IRedisList;
import org.qiunet.utils.data.CommonData;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.threadLocal.ThreadContextData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/2/10 17:57.
 */
public class EntityListDataSupport<PO extends IRedisList,VO> extends BaseDataSupport<PO> {
	protected IEntityListInfo entityInfo;

	public EntityListDataSupport(IEntityListInfo entityListInfo){
		super(new DbListSupport<PO>(), entityListInfo);

		this.entityInfo = entityListInfo;
		this.selectStatment = entityInfo.getNameSpace()+".get"+entityInfo.getClazz().getSimpleName()+"s";
	}
	/**
	 * update
	 * @param po 需要更新的对象po
	 */
	public void updatePo(PO po) {
		//  防止有的人直接insert . 没有获取列表. 导致redis出现脏数据
		this.getVoMap(entityInfo.getDbInfoKey(po));

		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po));

		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		List<PO> poList = new ArrayList(1);
		poList.add(po);
		entityInfo.getRedisUtil().setListToHash(key, poList);

		if (! entityInfo.needAsync()) {
			dbSupport.update(po, updateStatment);
		}else {
			entityInfo.getRedisUtil().saddString(entityInfo.getAsyncKey(entityInfo.getDbInfoKey(po)), entityInfo.getDbInfoKey(po) +"_" + entityInfo.getSubKey(po));
		}
	}
	/**
	 * insert po
	 * @param po 需要插入的po
	 * @return 1 表示成功
	 */
	public int insertPo(PO po){
		//  防止有的人直接insert . 没有获取列表. 导致redis出现脏数据
		this.getVoMap(entityInfo.getDbInfoKey(po));

		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		int ret = dbSupport.insert(po, insertStatment);

		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po));
		Map<Integer, VO> voMap = ThreadContextData.get(key);
		boolean insertToRedis = voMap != null;
		if (voMap != null) {
			voMap.put(entityInfo.getSubKey(po), (VO) entityInfo.getVo(po));
		}else {
			insertToRedis = entityInfo.getRedisUtil().exists(key);
		}
		if (insertToRedis) {
			List<PO> poList = new ArrayList(1);
			poList.add(po);
			entityInfo.getRedisUtil().setListToHash(key, poList);
		}
		return  ret;
	}
	/**
	 * 对缓存失效处理
	 * @param dbInfoKey 分库使用的key  一般uid 或者和platform配合使用
	 */
	public void expireCache(Object dbInfoKey) {
		String key = entityInfo.getRedisKey(dbInfoKey);
		ThreadContextData.removeKey(key);
		getRedis().expire(key, 0);
	}
	/**
	 * deletePo
	 * @param po 需要删除的po
	 */
	public void deletePo(PO po) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po));
		Map<Integer, VO> voMap = ThreadContextData.get(key);
		if (voMap != null) {
			voMap.remove(entityInfo.getSubKey(po));
		}
		List<PO> poList = new ArrayList(1);
		poList.add(po);
		entityInfo.getRedisUtil().deleteList(key, poList);

		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		dbSupport.delete(po, deleteStatment);
	}
	/**
	 * 取到一个map
	 * @param dbInfoKey 分库使用的key  一般uid 或者和platform配合使用
	 * @return po的VO对象
	 */
	public Map<Integer, VO> getVoMap(Object dbInfoKey){
		String key = entityInfo.getRedisKey(dbInfoKey);
		Map<Integer, VO> voMap = ThreadContextData.get(key);
		if (voMap != null) return  voMap;

		voMap = new HashMap<Integer, VO>();
		List<PO> poList = entityInfo.getRedisUtil().getListFromHash(key, entityInfo.getClazz());
		if (poList == null){
			poList = ((IDbList)dbSupport).selectList(selectStatment, entityInfo.getEntityDbInfo(dbInfoKey, 0));
			entityInfo.getRedisUtil().setListToHash(key, poList);
		}

		if (poList != null && !poList.isEmpty()) {
			for (PO po : poList) {
				voMap.put(entityInfo.getSubKey(po), (VO) entityInfo.getVo(po));
			}
		}
		ThreadContextData.put(key, voMap);
		return voMap;
	}

	@Override
	protected boolean updateToDb(String asyncValue) throws Exception {
		String vals[] = StringUtil.split(asyncValue, "_");
		String key = entityInfo.getRedisKey(vals[0]);
		PO po = (PO) entityInfo.getRedisUtil().getRedisObjectFromRedisList(key, entityInfo.getClazz(), vals[1]);

		if (po != null) {
			po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
			dbSupport.update(po, updateStatment);
			return true;
		}
		return false;
	}
}
