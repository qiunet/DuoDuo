package org.qiunet.utils.test.system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.system.SystemPropertyUtil;

import java.io.File;

/**
 * Created by qiunet.
 * 17/11/28
 */
public class TestSystemPropertyutil {
	@Test
	public void TestSystemPropertyutil(){
		Assertions.assertEquals(File.separator, SystemPropertyUtil.getFileSeparator());
		Assertions.assertEquals(File.pathSeparator, SystemPropertyUtil.getPathSeparator());

		Assertions.assertEquals(System.getProperty("user.dir"), SystemPropertyUtil.getUserDir());
		Assertions.assertEquals(System.getProperty("user.home"), SystemPropertyUtil.getUserHome());

		Assertions.assertEquals(System.getProperty("line.separator"), SystemPropertyUtil.getLineSeparator());

	}
}
