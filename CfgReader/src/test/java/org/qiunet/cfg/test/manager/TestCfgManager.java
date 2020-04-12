package org.qiunet.cfg.test.manager;

import org.junit.Test;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.utils.classScanner.ClassScanner;

/***
 *
 * @author qiunet
 * 2020-02-05 21:09
 **/
public class TestCfgManager {
	@Test
	public void test(){
		ClassScanner.getInstance().scanner();
		try {
			CfgManagers.getInstance().reloadSetting();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
