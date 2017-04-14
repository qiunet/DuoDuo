package org.qiunet.template.equip.entity;

import org.qiunet.data.redis.support.PlatformRedisList;
import org.apache.ibatis.type.Alias;
/**
 * 对象为自动创建
 */
@Alias("EquipPo")
public class EquipPo extends PlatformRedisList {
    public static final String FIELD_UID = "uid";
    public static final String FIELD_ID = "id";
    public static final String FIELD_EXP = "exp";
    public static final String FIELD_LEVEL = "level";
    private static final String [] fields = {FIELD_EXP ,FIELD_LEVEL };
    private int uid;                             /** uid   */
    private int id;                              /** 设定id  */
    private int exp;                             /** 经验  */
    private int level;                           /** 等级  */
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    @Override
    public String getSubKey() {
        return FIELD_ID;
    }
    @Override
    public int getSubId() {
        return id;
    }
}
