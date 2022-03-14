package org.qiunet.utils.test.system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.qiunet.utils.system.ExecCommandUtil;
import org.qiunet.utils.test.base.BaseTest;

/**
 * @author qiunet
 *         Created on 16/11/6 13:10.
 */
public class TestExecCommandUtil extends BaseTest{

	@Test
	@EnabledOnOs({OS.LINUX, OS.MAC})
	public void testExecShell(){
		String ret = ExecCommandUtil.exec("ls", "/Users");
		logger.info(ret);
		Assertions.assertNotNull(ret);
	}
}
