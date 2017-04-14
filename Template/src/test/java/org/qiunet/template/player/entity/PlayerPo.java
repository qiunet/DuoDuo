package org.qiunet.template.player.entity;

import org.qiunet.data.redis.support.PlatformRedisEntity;
import org.apache.ibatis.type.Alias;
/**
 * 对象为自动创建
 */
@Alias("PlayerPo")
public class PlayerPo extends PlatformRedisEntity {
    public static final String FIELD_UID = "uid";
    public static final String FIELD_EXP = "exp";
    public static final String FIELD_LEVEL = "level";
    private static final String [] fields = {FIELD_EXP ,FIELD_LEVEL };
    private int uid;                             /** uid   */
    private int exp;                             /** 经验  */
    private int level;                           /** 等级  */
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public int getExp() {
        return exp;
    }
    public void setExp(int exp) {
        this.exp = exp;
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
        return FIELD_UID;
    }
}
