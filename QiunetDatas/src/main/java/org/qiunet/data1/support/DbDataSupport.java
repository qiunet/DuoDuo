package org.qiunet.data1.support;

import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntity;

import java.util.Map;

public class DbDataSupport<Key, Po extends IDbEntity<Key, Vo>, Vo extends IEntityVo<Po>> extends BaseDbDataSupport<Po, Vo> {
	public DbDataSupport(Class<Po> poClass, VoSupplier<Po, Vo> supplier) {
		super(poClass, supplier);

	}

	public Vo getVo(Key key) {
		Map<String, Object> map = SelectMap.create().put(defaultPo.keyFieldName(), key);
		Po po = DefaultDatabaseSupport.getInstance().selectOne(selectStatement, map);
		if (po == null) return null;

		return supplier.get(po);
	}
}
