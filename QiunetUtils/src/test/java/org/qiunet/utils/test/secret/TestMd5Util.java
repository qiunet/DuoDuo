package org.qiunet.utils.test.secret;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.secret.MD5Util;

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
		URL resource = MD5Util.class.getResource("/db.properties");
		Path path = Paths.get(resource.toURI());

		Assert.assertEquals("ebbcf0416182a049109d07f5a1b57cc4", MD5Util.encrypt(path.toFile()));
	}

}
