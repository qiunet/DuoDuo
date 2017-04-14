package org.qiunet.template.qunxiu.entity;

import org.qiunet.data.redis.support.RedisEntity;
import org.apache.ibatis.type.Alias;
/**
 * 对象为自动创建
 */
@Alias("QunxiuPo")
public class QunxiuPo extends RedisEntity {
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_MASTER = "master";
    public static final String FIELD_LEVEL = "level";
    private static final String [] fields = {FIELD_NAME ,FIELD_MASTER ,FIELD_LEVEL };
    private int id;                              /** id  */
    private String name;                         /** 名称  */
    private int master;                          /** 主管uid  */
    private int level;                           /** 等级  */
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
    public int getMaster() {
        return master;
    }
    public void setMaster(int master) {
        this.master = master;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
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
