package org.qiunet.template.item.entity;

import org.qiunet.data.redis.support.RedisList;
import org.apache.ibatis.type.Alias;
/**
 * 对象为自动创建
 */
@Alias("ItemPo")
public class ItemPo extends RedisList {
    public static final String FIELD_UID = "uid";
    public static final String FIELD_ITEM_ID = "item_id";
    public static final String FIELD_ITEM_COUNT = "item_count";
    private static final String [] fields = {FIELD_ITEM_COUNT };
    private int uid;                             /** uid  */
    private int item_id;                         /** 道具id  */
    private int item_count;                      /** 道具数量  */
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public int getItem_id() {
        return item_id;
    }
    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }
    public int getItem_count() {
        return item_count;
    }
    public void setItem_count(int item_count) {
        this.item_count = item_count;
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
        return FIELD_ITEM_ID;
    }
    @Override
    public int getSubId() {
        return item_id;
    }
}
