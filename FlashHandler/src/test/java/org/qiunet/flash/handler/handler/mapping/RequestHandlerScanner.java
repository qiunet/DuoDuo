package org.qiunet.flash.handler.handler.mapping;

import org.junit.BeforeClass;
import org.qiunet.flash.handler.common.annotation.support.RequestScannerHandler;
import org.qiunet.flash.handler.gamecfg.GiftCfg;
import org.qiunet.utils.classScanner.ScannerAllClassFile;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * 自行加载requestHandler
 * Created by qiunet.
 * 17/11/22
 */

public abstract class RequestHandlerScanner {
	private static boolean inited;
	@BeforeClass
	public static void initHandler() {
		if (inited) return;

		inited = true;
		ScannerAllClassFile scannerAllClassFile = new ScannerAllClassFile();

		try {
			scannerAllClassFile.scannerJarFile(GiftCfg.class.getResource("").toURI().toURL());
			scannerAllClassFile.addScannerHandler(new RequestScannerHandler());

			scannerAllClassFile.scanner();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
