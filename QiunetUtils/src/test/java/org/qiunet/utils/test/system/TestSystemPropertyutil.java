package org.qiunet.utils.test.system;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.system.SystemPropertyUtil;

import java.io.File;

/**
 * Created by qiunet.
 * 17/11/28
 */
public class TestSystemPropertyutil {
	@Test
	public void TestSystemPropertyutil(){
		Assert.assertEquals(File.separator, SystemPropertyUtil.getFileSeparator());
		Assert.assertEquals(File.pathSeparator, SystemPropertyUtil.getPathSeparator());

		Assert.assertEquals(System.getProperty("user.dir"), SystemPropertyUtil.getUserDir());
		Assert.assertEquals(System.getProperty("user.home"), SystemPropertyUtil.getUserHome());

		Assert.assertEquals(System.getProperty("line.separator"), SystemPropertyUtil.getLineSeparator());

	}
}
