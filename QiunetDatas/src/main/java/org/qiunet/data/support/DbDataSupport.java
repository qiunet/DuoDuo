package org.qiunet.data.support;

import org.qiunet.data.core.select.DbParamMap;
import org.qiunet.data.db.entity.IDbEntity;

import java.util.Map;

public class DbDataSupport<Key, Do extends IDbEntity<Key>, Bo extends IEntityBo<Do>> extends BaseDbDataSupport<Key, Do, Bo> {

	public DbDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);

	}

	public Bo getBo(Key key) {
		Map<String, Object> map = DbParamMap.create(table, table.keyName(), key);
		Do aDo = databaseSupport().selectOne(selectStatement, map);
		if (aDo == null) return null;

		return this.setEntity2NormalStatus(supplier.get(aDo));
	}

	/**
	 * 删除
	 * @param bo
	 */
	@Override
	public void delete(Bo bo) {
		this.deleteByKey(bo.getDo().key());
	}
	/***
	 * 删除指定key的do
	 * @param key
	 */
	public void deleteByKey(Key key) {
		DbParamMap map = DbParamMap.create(table, table.keyName(), key);
		databaseSupport().delete(deleteStatement, map);
	}
}
