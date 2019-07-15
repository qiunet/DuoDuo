package org.qiunet.data1.support;

import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntity;

import java.util.Map;

public class DbDataSupport<Key, Po extends IDbEntity<Key>> extends BaseDbDataSupport<Po> {

	public DbDataSupport(Class<Po> poClass) {
		super(poClass);
	}

	public Po getPo(Key key) {
		Map<String, Object> map = SelectMap.create().put(defaultPo.getKeyFieldName(), key);
		Po po = DefaultDatabaseSupport.getInstance().selectOne(selectStatement, map);
		if (po != null) po.setDataSupport(this);
		return po;
	}
}
