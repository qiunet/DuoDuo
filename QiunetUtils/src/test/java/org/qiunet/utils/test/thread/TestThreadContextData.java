package org.qiunet.utils.test.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.test.base.BaseTest;
import org.qiunet.utils.thread.ThreadContextData;

/**
 * @author qiunet
 *         Created on 16/11/5 09:01.
 */
public class TestThreadContextData extends BaseTest{
	@Test
	public void exec() {

		ThreadContextData.put("qiunet", 1111);

		int ret =  ThreadContextData.get("qiunet");

		Assertions.assertTrue(1111  == ret);
	}
}
