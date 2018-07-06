package org.qiunet.data.core.support;

import javafx.application.Platform;
import org.qiunet.data.core.support.entityInfo.IPlatformEntityListInfo;
import org.qiunet.data.db.support.base.DbListSupport;
import org.qiunet.data.db.support.base.IDbList;
import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.support.info.IPlatformRedisList;
import org.qiunet.utils.data.CommonData;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.threadLocal.ThreadContextData;

import java.util.*;

/**
 * @author qiunet
 *         Created on 17/2/11 08:46.
 */
public class PlatformEntityListDataSupport<DbInfoKey, SubKey, PO extends IPlatformRedisList, VO> extends BaseDataSupport<PO> {
	private IPlatformEntityListInfo<DbInfoKey, SubKey, PO, VO> entityInfo;

	public PlatformEntityListDataSupport(IPlatformEntityListInfo<DbInfoKey, SubKey, PO, VO> entityInfo) {
		super(new DbListSupport(), entityInfo);

		this.entityInfo = entityInfo;

		this.selectStatment = entityInfo.getNameSpace() + ".get"+entityInfo.getClazz().getSimpleName() +"s";
	}
	/**
	 * update
	 * @param po 需要更新的对象po
	 */
	public void updatePo(PO po) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po), po.getPlatform());

		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		List<PO> poList = new ArrayList(1);
		poList.add(po);
		entityInfo.getRedisUtil().setListToHash(key, poList);

		if (! entityInfo.needAsync()) {
			dbSupport.update(po, updateStatment);
		}else {
			String asyncValue = entityInfo.getDbInfoKey(po) +"_"+po.getPlatformName()+"_"+po.getSubId();
			entityInfo.getRedisUtil().returnJedisProxy().sadd(entityInfo.getAsyncKey(entityInfo.getDbInfoKey(po)),  asyncValue);
		}

	}
	/**
	 * insert po
	 * @param po 需要插入的po
	 * @return 1 表示成功
	 */
	public VO insertPo(PO po){
		// 防止有人误操作. insert时候. 没有得到所有的列表.
		this.getVoMap(entityInfo.getDbInfoKey(po), po.getPlatform());

		po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
		dbSupport.insert(po, insertStatment);

		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po), po.getPlatform());
		Map<SubKey, VO> voMap = ThreadContextData.get(key);
		VO vo = entityInfo.getVo(po);
		boolean insertRedis = voMap != null;
		if (voMap != null) {
			voMap.put(entityInfo.getSubKey(po), vo);
		}else {
			insertRedis = entityInfo.getRedisUtil().returnJedisProxy().exists(key);
		}

		if (insertRedis) {
			List<PO> poList = new ArrayList<>(1);
			poList.add(po);
			entityInfo.getRedisUtil().setListToHash(key, poList);
		}
		return vo;
	}
	/**
	 * 对缓存失效处理
	 */
	public void expireCache(DbInfoKey dbInfoKey, PlatformType platform) {
		String key = entityInfo.getRedisKey(dbInfoKey, platform);
		ThreadContextData.removeKey(key);
		getRedis().returnJedisProxy().expire(key, 0);
	}
	/**
	 * deletePo
	 * @param po 需要删除的po
	 */
	public void deletePo(PO po) {
		String key = entityInfo.getRedisKey(entityInfo.getDbInfoKey(po), po.getPlatform());

		Map<SubKey, VO> voMap = ThreadContextData.get(key);
		if (voMap != null) {
			voMap.remove(po.getSubId());
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
	 * @param platform 平台
	 * @return po的VO对象
	 */
	public Map<SubKey, VO> getVoMap(DbInfoKey dbInfoKey, PlatformType platform){
		String key = entityInfo.getRedisKey(dbInfoKey, platform);
		Map<SubKey, VO> voMap = ThreadContextData.get(key);
		if (voMap != null) return  voMap;

		voMap = new HashMap<>();
		List<PO> poList = entityInfo.getRedisUtil().getListFromHash(key, entityInfo.getClazz());
		if (poList == null) {
			poList = ((IDbList)dbSupport).selectList(selectStatment, entityInfo.getEntityDbInfo(dbInfoKey, platform, null));
			if (poList != null){
				entityInfo.getRedisUtil().setListToHash(key, poList);
			}
		}
		if (poList != null && !poList.isEmpty()) {
			for (PO po : poList) {
				po.setPlatform(platform);
				po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
				voMap.put(entityInfo.getSubKey(po), entityInfo.getVo(po));
			}
		}
		ThreadContextData.put(key, voMap);
		return voMap;
	}

	@Override
	protected boolean updateToDb(String asyncValue) throws Exception {
		String vals[] = StringUtil.split(asyncValue, "_");
		PlatformType platformType = PlatformType.parse(vals[1]);

		String key = entityInfo.getRedisKey(vals[0], platformType);
		PO po = entityInfo.getRedisUtil().getRedisObjectFromRedisList(key, entityInfo.getClazz(), vals[2]);

		if (po != null) {
			po.setEntityDbInfo(entityInfo.getEntityDbInfo(po));
			po.setPlatform(platformType);
			dbSupport.update(po, updateStatment);
			return true;
		}
		return false;
	}
}
