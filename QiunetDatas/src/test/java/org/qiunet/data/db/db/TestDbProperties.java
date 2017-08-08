package org.qiunet.data.db.db;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data.db.util.DbProperties;

/**
 * Created by qiunet.
 * 17/8/8
 */
public class TestDbProperties {

	@Test
	public void dbProperties () {
		Assert.assertTrue(DbProperties.getInstance().getLoginNeedDb() == 100);
	}
}
