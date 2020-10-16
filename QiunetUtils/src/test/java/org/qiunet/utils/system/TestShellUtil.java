package org.qiunet.utils.system;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;

/**
 * @author qiunet
 *         Created on 16/11/6 13:10.
 */
public class TestShellUtil extends BaseTest{
	@Test
	public void testExecShell(){
		String[] cmd = {"ls"};
		String ret = ShellUtil.execShell(cmd);
		logger.info(ret);
		Assert.assertNotNull(ret);
	}
}
