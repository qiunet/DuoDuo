package org.qiunet.utils.encryptAndDecrypt;

import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author qiunet
 *         Created on 16/11/6 13:53.
 */
public class TestMd5Util {

	@Test
	public void test() throws Exception {
		URL resource = MD5Util.class.getResource("/ChangeClass.class");
		Path path = Paths.get(resource.toURI());

		Assert.assertEquals("5bb9aa1df90235350bd392b6dba4face", MD5Util.encrypt(path.toFile()));
	}

}
