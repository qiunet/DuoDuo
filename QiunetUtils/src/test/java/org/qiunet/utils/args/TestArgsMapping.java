package org.qiunet.utils.args;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by qiunet.
 * 17/10/10
 */
public class TestArgsMapping {
	@Test
	public void testArgs(){
		String [] args = {"--qiunet=qiuyang", "qiu=yang", "--qiunet1=qiuyang1"};

		ArgsMapping mapping = new ArgsMapping(args);

		Assert.assertEquals(mapping.getValue("qiunet"), "qiuyang");
		Assert.assertEquals(mapping.getValue("qiunet1"), "qiuyang1");
		Assert.assertNull(mapping.getValue("qiu"));
	}
}
