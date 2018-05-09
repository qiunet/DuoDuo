package org.qiunet.flash.handler.handler.mapping;

import org.junit.BeforeClass;
import org.qiunet.flash.handler.common.annotation.support.RequestScannerHandler;
import org.qiunet.utils.classScanner.ScannerAllClassFile;


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
//			scannerAllClassFile.scannerJarFile(GiftCfg.class.getResource("").toURI().toURL());
			scannerAllClassFile.addScannerHandler(new RequestScannerHandler());

			scannerAllClassFile.scanner();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
