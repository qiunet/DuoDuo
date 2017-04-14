package org.qiunet.template.global.entity;

import org.qiunet.data.redis.support.RedisEntity;
import org.apache.ibatis.type.Alias;
/**
 * 对象为自动创建
 */
@Alias("GlobalTablePo")
public class GlobalTablePo extends RedisEntity {
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    private static final String [] fields = {FIELD_NAME };
    private int id;                              /** id  */
    private String name;                         /** 名称  */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    protected String[] getFields() {
        return fields;
    }
    @Override
    public String getDbInfoKeyName() {
        return FIELD_ID;
    }
}
