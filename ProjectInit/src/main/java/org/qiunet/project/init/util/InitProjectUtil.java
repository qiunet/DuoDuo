package org.qiunet.project.init.util;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/***
 *
 *
 * qiunet
 * 2019-08-19 16:52
 ***/
public final class InitProjectUtil {
	/**
	 * 得到真实的user.dir
	 * @return
	 */
	public static File getRealUserDir() {
		File realUserDir = null;
		try {
			Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource("").toURI());
			realUserDir = path.toFile().getParentFile().getParentFile();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return realUserDir;
	}
}
