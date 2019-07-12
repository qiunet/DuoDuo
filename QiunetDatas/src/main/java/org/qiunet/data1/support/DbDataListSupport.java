package org.qiunet.data1.support;

import org.qiunet.data1.core.select.SelectMap;
import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntityList;

import java.util.List;

public class DbDataListSupport<Key, SubKey, Po extends IDbEntityList<Key, SubKey>> extends BaseDbDataSupport<Po> {
	/***
	 * 取到一个poList
	 * @param key
	 * @return
	 */
	public List<Po> getPoList(Key key) {
		SelectMap map = SelectMap.create().put(defaultPo.getKeyFieldName(), key);
		List<Po> poList = DefaultDatabaseSupport.getInstance().selectList(selectStatement, map);
		poList.forEach(po -> po.setDataSupport(this));
		return poList;
	}
}
