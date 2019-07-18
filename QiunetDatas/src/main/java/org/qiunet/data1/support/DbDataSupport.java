package org.qiunet.data1.support;

import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntity;

import java.util.Map;

public class DbDataSupport<Key, Po extends IDbEntity<Key, Bo>, Bo extends IEntityBo<Po>> extends BaseDbDataSupport<Po, Bo> {
	public DbDataSupport(Class<Po> poClass, BoSupplier<Po, Bo> supplier) {
		super(poClass, supplier);

	}

	public Bo getBo(Key key) {
		Map<String, Object> map = SelectMap.create().put(defaultPo.keyFieldName(), key);
		Po po = DefaultDatabaseSupport.getInstance().selectOne(selectStatement, map);
		if (po == null) return null;

		return supplier.get(po);
	}
}
