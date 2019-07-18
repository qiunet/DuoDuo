package org.qiunet.data1.db.entity;

import org.qiunet.data1.support.IEntityBo;

/***
 *
 * @param <Key>
 * @param <SubKey>
 */
public abstract class DbEntityList<Key, SubKey, Bo extends IEntityBo> extends DbEntity<Key, Bo> implements IDbEntityList<Key, SubKey, Bo> {

}
