package org.qiunet.template.equip.info;

import redis.RedisDataUtil;
import org.qiunet.data.db.support.info.uidinfo.UidPlatformEntityListDbInfo;
import redis.key.RedisKey;
import org.qiunet.data.core.support.entityInfo.IPlatformEntityListInfo;
import org.qiunet.template.equip.entity.EquipVo;
import org.qiunet.template.equip.entity.EquipPo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.IPlatformEntityListDbInfo;   
import org.qiunet.data.enums.PlatformType;

public class EquipEntityInfo implements IPlatformEntityListInfo<EquipPo, EquipVo> {
	
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
    public EquipVo getVo(EquipPo po) {
        return new EquipVo(po);
    }

    @Override
    public AbstractRedisUtil getRedisUtil() {
      return RedisDataUtil.getInstance();
    }

    @Override
    public Object getDbInfoKey(EquipPo po) {
     return po.getUid();
    }

    @Override
    public String getAsyncKey(Object dbInfoKey) {
        return RedisKey.EQUIP.getAsyncKey(dbInfoKey);
    }

    @Override
    public Integer getSubKey(EquipPo po) {
        return po.getSubId();
    }
    
    @Override
    public IEntityDbInfo getEntityDbInfo(EquipPo po) {
        return getEntityDbInfo(getDbInfoKey(po), po.getPlatform(), getSubKey(po));
    }
    
    @Override
    public IPlatformEntityListDbInfo getEntityDbInfo(Object dbInfoKey, PlatformType platform, int subId) {
         return new UidPlatformEntityListDbInfo(dbInfoKey, platform, subId);
    }

    @Override
    public String getRedisKey(Object dbInfoKey, PlatformType platform) {
        return RedisKey.EQUIP.getKey(dbInfoKey, platform);
    }
}