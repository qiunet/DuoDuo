package org.qiunet.cfg.test.manager;

import org.junit.jupiter.api.Test;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 * @author qiunet
 * 2020-02-05 21:09
 **/
public class TestCfgManager {
	@Test
	public void test() throws Exception {
		ClassScanner.getInstance(ScannerType.CFG).scanner();
		try {
			CfgManagers.getInstance().reloadSetting();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
