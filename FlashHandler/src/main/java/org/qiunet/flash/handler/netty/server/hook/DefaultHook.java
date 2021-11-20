package org.qiunet.flash.handler.netty.server.hook;

import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.utils.classLoader.ClassHotSwap;
import org.qiunet.utils.logger.LoggerType;

import java.nio.file.Paths;

/***
 *  默认的hook实现
 *
 * @author qiunet
 * 2021/11/20 23:43
 */
public class DefaultHook implements Hook {

	@Override
	public void shutdown() {
	}

	@Override
	public void custom(String msg) {
		switch (msg) {
			case "hotSwap":
				ClassHotSwap.hotSwap(Paths.get(System.getProperty("hotSwap.dir")));
				break;
		}
	}
}
