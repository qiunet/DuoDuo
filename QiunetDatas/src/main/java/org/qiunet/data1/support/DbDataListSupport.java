package org.qiunet.data1.support;

import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntityList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbDataListSupport<Key, SubKey, Po extends IDbEntityList<Key, SubKey>, Vo> extends BaseDbDataSupport<Po, Vo> {
	public DbDataListSupport(Class<Po> poClass, VoSupplier<Po, Vo> supplier) {
		super(poClass, supplier);
	}

	/***
	 * 取到一个poList
	 * @param key
	 * @return
	 */
	public Map<SubKey, Vo> getVoMap(Key key) {
		SelectMap map = SelectMap.create().put(defaultPo.keyFieldName(), key);
		List<Po> poList = DefaultDatabaseSupport.getInstance().selectList(selectStatement, map);
		if (poList == null) return Collections.emptyMap();

		return poList.parallelStream().collect(Collectors.toMap(Po::subKey, po -> supplier.get(po)));
	}
}
