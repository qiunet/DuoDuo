package org.qiunet.utils.test.args;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.args.ArgsMapping;

/**
 * Created by qiunet.
 * 17/10/10
 */
public class TestArgsMapping {
	@Test
	public void testArgs(){
		String [] args = {"--qiunet=qiuyang", "qiu=yang", "--qiunet1=qiuyang1"};

		ArgsMapping mapping = new ArgsMapping(args);
		mapping.addArgsDesc("q", "qiunet", "命令说明1")
						.addArgsDesc("y", "yang", "命令说明2");

		Assertions.assertEquals(mapping.getValue("qiunet"), "qiuyang");
		Assertions.assertEquals(mapping.getValue("qiunet1"), "qiuyang1");
		Assertions.assertNull(mapping.getValue("qiu"));
	}
}
