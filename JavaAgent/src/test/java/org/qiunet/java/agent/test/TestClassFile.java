package org.qiunet.java.agent.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qAgent.ClassFile;

import java.io.IOException;
import java.io.InputStream;

/***
 *
 * @author qiunet
 * 2022/1/6 13:50
 */
public class TestClassFile {

	@Test
	public void test() throws IOException {
		try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("ProfilePrinter.class")) {
			ClassFile classFile = new ClassFile(stream);
			Assertions.assertEquals(classFile.getClassName(), "org.qiunet.profile.ProfilePrinter");
		}
	}
}
