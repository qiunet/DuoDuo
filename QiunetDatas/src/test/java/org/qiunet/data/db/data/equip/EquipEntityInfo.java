package org.qiunet.data.db.data.equip;

import org.qiunet.data.core.support.entityInfo.IPlatformEntityListInfo;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.IPlatformEntityListDbInfo;
import org.qiunet.data.db.support.info.uidinfo.UidPlatformEntityListDbInfo;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.base.RedisDataUtil;
import org.qiunet.data.redis.entity.EquipPo;
import org.qiunet.data.redis.key.RedisKey;

/**
 * @author qiunet
 *         Created on 17/2/14 10:44.
 */
public class EquipEntityInfo implements IPlatformEntityListInfo<Integer, Integer, EquipPo, EquipVo> {
	@Override
	public String getNameSpace() {
		return "equip";
	}

	@Override
	public Class<EquipPo> getClazz() {
		return EquipPo.class;
	}

	@Override
	public boolean needAsync() {
		return true;
	}

	@Override
	public EquipVo getVo(EquipPo equipPo) {
		return new EquipVo(equipPo);
	}

	@Override
	public AbstractRedisUtil getRedisUtil() {
		return RedisDataUtil.getInstance();
	}

	@Override
	public Integer getDbInfoKey(EquipPo equipPo) {
		return equipPo.getUid();
	}

	@Override
	public String getAsyncKey(Object dbInfoKey) {
		return RedisKey.EQUIP.getAsyncKey(dbInfoKey);
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(EquipPo equipPo) {
		return getEntityDbInfo(getDbInfoKey(equipPo), equipPo.getPlatform(), getSubKey(equipPo));
	}

	@Override
	public IPlatformEntityListDbInfo getEntityDbInfo(Integer dbInfoKey, PlatformType platform, Integer subId) {
		return new UidPlatformEntityListDbInfo(dbInfoKey, platform, subId);
	}

	@Override
	public Integer getSubKey(EquipPo equipPo) {
		return equipPo.getSubId();
	}

	@Override
	public String getRedisKey(Object dbInfoKey, PlatformType platform) {
		return RedisKey.EQUIP.getKeyByParams(dbInfoKey, platform);
	}
}
