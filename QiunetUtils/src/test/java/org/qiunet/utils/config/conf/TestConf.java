package org.qiunet.utils.config.conf;

import org.qiunet.utils.config.anno.DConfig;
import org.qiunet.utils.config.anno.DConfigValue;

/***
 *
 *
 * @author qiunet
 * 2020-12-24 20:59
 */
@DConfig("test.conf")
public class TestConf {

	@DConfigValue(value = "server.server_port")
	private static int port;

	public static int getPort() {
		return port;
	}
}
