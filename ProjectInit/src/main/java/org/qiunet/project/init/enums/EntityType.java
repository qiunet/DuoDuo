package org.qiunet.project.init.enums;

import org.qiunet.data.core.support.EntityDataSupport;
import org.qiunet.data.core.support.EntityListDataSupport;
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
    RedisEntity(        EntityInfoType.IEntityInfo,             RedisEntity.class,          DbInfoType.IEntityDbInfo, 				EntityDataSupport.class),
    RedisList(          EntityInfoType.IEntityListInfo,         RedisList.class,            DbInfoType.IEntityListDbInfo, 			EntityListDataSupport.class),
    ;
    private EntityInfoType infoType;
    private Class clazz;
    private DbInfoType dbInfoType;
    private Class<?> dataSupportClass;
    private EntityType(EntityInfoType infoType, Class clazz, DbInfoType dbInfoType, Class<?> dataSupportClass) {
        this.clazz = clazz;
        this.infoType = infoType;
        this.dbInfoType = dbInfoType;
        this.dataSupportClass = dataSupportClass;
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

	public Class<?> getDataSupportClass() {
		return dataSupportClass;
	}

    /***
     * 是否是list类型
     * @return true 是
     */
    public boolean isListType(){
        return this == RedisList;
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
