package org.qiunet.data1.db.entity;

import org.qiunet.data1.support.IEntityVo;

/***
 *
 * @param <Key>
 * @param <SubKey>
 */
public abstract class DbEntityList<Key, SubKey, Vo extends IEntityVo> extends DbEntity<Key, Vo> implements IDbEntityList<Key, SubKey, Vo> {

}
