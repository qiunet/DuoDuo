package project.init.enums;

import org.qiunet.data.redis.support.PlatformRedisEntity;
import org.qiunet.data.redis.support.PlatformRedisList;
import org.qiunet.data.redis.support.RedisEntity;
import org.qiunet.data.redis.support.RedisList;
import org.qiunet.utils.exceptions.EnumParseException;

/**
 *  po 的4个继承类.
 *  以及能用关联的数据
 * @author qiunet
 *         Created on 17/2/14 18:25.
 */
public enum  EntityType {
    RedisEntity(        EntityInfoType.IEntityInfo,             RedisEntity.class,          DbInfoType.IEntityDbInfo),
    PlatformRedisEntity(EntityInfoType.IPlatformEntityInfo,     PlatformRedisEntity.class,  DbInfoType.IPlatformEntityDbInfo),
    RedisList(          EntityInfoType.IEntityListInfo,         RedisList.class,            DbInfoType.IEntityListDbInfo),
    PlatformRedisList(  EntityInfoType.IPlatformEntityListInfo, PlatformRedisList.class,    DbInfoType.IPlatformEntityListDbInfo),
    ;
    private EntityInfoType infoType;
    private Class clazz;
    private DbInfoType dbInfoType;
    private EntityType(EntityInfoType infoType, Class clazz, DbInfoType dbInfoType) {
        this.clazz = clazz;
        this.infoType = infoType;
        this.dbInfoType = dbInfoType;
    }

    public Class getClazz(){
        return clazz;
    }

    public DbInfoType getDbInfoType() {
        return dbInfoType;
    }

    public EntityInfoType getInfoType() {
        return infoType;
    }

    /**
     * 是否是平台类型
     * @return true 是
     */
    public boolean isPlatformType(){
        return this == PlatformRedisList || this == PlatformRedisEntity;
    }
    /***
     * 是否是list类型
     * @return true 是
     */
    public boolean isListType(){
        return this == RedisList || this == PlatformRedisList;
    }

    /**
     * 匹配对应的EntityType
     * @param val 值
     * @return EntityType
     */
    public static EntityType parse(String val){
        for(EntityType t : values())
            if(t.toString().equals(val))
                return t;

        throw new EnumParseException(val);
    }
}
