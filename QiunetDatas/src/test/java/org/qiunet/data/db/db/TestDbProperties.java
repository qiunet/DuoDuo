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
		Assert.assertTrue(DbProperties.getInstance().getLoginNeedDb() == 1);
	}
	@Test
	public void testUidIsValid(){
		Assert.assertFalse(DbProperties.getInstance().isValidId(120));
		Assert.assertFalse(DbProperties.getInstance().isValidId(110));
		Assert.assertTrue(DbProperties.getInstance().isValidId(109));
		Assert.assertTrue(DbProperties.getInstance().isValidId(100));
	}
}
