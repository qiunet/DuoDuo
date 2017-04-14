package org.qiunet.utils.shell;

import org.junit.Assert;
import org.qiunet.utils.base.BaseTest;
import org.junit.Test;

/**
 * @author qiunet
 *         Created on 16/11/6 13:10.
 */
public class TestShellUtil extends BaseTest{
	@Test
	public void testExecShell(){
		String cmd [] = {"ls"};
		String ret = ShellUtil.execShell(cmd);
		logger.info(ret);
		Assert.assertNotNull(ret);
	}
}
