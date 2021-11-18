package org.qiunet.data.support;

import com.google.common.collect.Maps;
import org.qiunet.data.core.select.DbParamMap;
import org.qiunet.data.db.entity.IDbEntityList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbDataListSupport<Key, SubKey, Do extends IDbEntityList<Key, SubKey, Bo>, Bo extends IEntityBo<Do>> extends BaseDbDataSupport<Do, Bo> {
	public DbDataListSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
	}

	/***
	 * 取到一个poList
	 * @param key
	 * @return
	 */
	public Map<SubKey, Bo> getBoMap(Key key) {
		DbParamMap map = DbParamMap.create(table, defaultDo.keyFieldName(), key);
		List<Do> doList = databaseSupport().selectList(selectStatement, map);
		if (doList == null) return Maps.newHashMap();

		return doList.stream().collect(Collectors.toMap(Do::subKey, aDo -> supplier.get(aDo)));
	}
	/**
	 * 删除
	 * @param aDo
	 */
	@Override
	public void delete(Do aDo) {
		this.delete(aDo.key(), aDo.subKey());
	}

	public void delete(Key key, SubKey subKey) {
		DbParamMap map = DbParamMap.create(table, defaultDo.keyFieldName(), key)
			.put(defaultDo.subKeyFieldName(), subKey);

		databaseSupport().delete(deleteStatement, map);
	}
}
