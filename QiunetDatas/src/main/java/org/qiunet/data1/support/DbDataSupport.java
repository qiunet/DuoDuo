package org.qiunet.data1.support;

import org.qiunet.data1.core.select.DbParamMap;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntity;

import java.util.Map;

public class DbDataSupport<Key, Do extends IDbEntity<Key, Bo>, Bo extends IEntityBo<Do>> extends BaseDbDataSupport<Do, Bo> {
	public DbDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);

	}

	public Bo getBo(Key key) {
		Map<String, Object> map = DbParamMap.create().put(defaultDo.keyFieldName(), key);
		Do aDo = DefaultDatabaseSupport.getInstance().selectOne(selectStatement, map);
		if (aDo == null) return null;

		return supplier.get(aDo);
	}


	/**
	 * 删除
	 * @param aDo
	 */
	@Override
	public void delete(Do aDo) {
		this.delete(aDo.key());
	}
	/***
	 * 删除指定key的do
	 * @param key
	 */
	public void delete(Key key) {
		DbParamMap map = DbParamMap.create().put(defaultDo.keyFieldName(), key);
		DefaultDatabaseSupport.getInstance().delete(deleteStatement, map);
	}
}
