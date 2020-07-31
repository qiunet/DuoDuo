package org.qiunet.utils.threadLocal;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;

/**
 * @author qiunet
 *         Created on 16/11/5 09:01.
 */
public class TestThreadContextData extends BaseTest{
	@Test
	public void exec() {

		ThreadContextData.put("qiunet", 1111);

		int ret =  ThreadContextData.get("qiunet");

		Assert.assertTrue(1111  == ret);
	}
}
