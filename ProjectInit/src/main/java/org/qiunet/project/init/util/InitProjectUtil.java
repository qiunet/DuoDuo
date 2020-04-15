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
	private static File realUseDir = getDefaultUseDir();
	/**
	 * 得到真实的user.dir
	 * @return
	 */
	public static File getRealUserDir() {
		return realUseDir;
	}

	public static void setRealUseDir(File realUseDir) {
		InitProjectUtil.realUseDir = realUseDir;
	}

	public static File getDefaultUseDir(){
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
