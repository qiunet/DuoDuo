package org.qiunet.flash.handler.gamecfg;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.annotation.support.GameCfgScannerHandler;
import org.qiunet.flash.handler.common.annotation.support.PropertiesScannerHandler;
import org.qiunet.utils.classScanner.ScannerAllClassFile;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class TestGameCfgManager {
	/***
	 *
	 */
	@Test
	public void testGameCfgManager() throws Exception {
		ScannerAllClassFile scannerAllClassFile = new ScannerAllClassFile();

		scannerAllClassFile.scannerJarFile(GiftCfg.class.getResource("").toURI().toURL());
		scannerAllClassFile.addScannerHandler(new GameCfgScannerHandler());
		scannerAllClassFile.addScannerHandler(new PropertiesScannerHandler());

		scannerAllClassFile.scanner();

		Assert.assertTrue(GameCfgManagers.getInstance().cfgSize() == 4);
		Assert.assertTrue(GameCfgManagers.getInstance().propertySize() == 1);

		GameCfgManagers.getInstance().initSetting();
		GameCfgManagers.getInstance().loadDataSetting();

		Assert.assertEquals(300, InitManager.getInstance().getIntValue(10));
		Assert.assertEquals("[{\"count\":20,\"cfgId\":100002}]", InitManager.getInstance().getValue(24));

		Assert.assertEquals(3, GiftManager.getInstance().getGiftcfgList("203001").size());
		Assert.assertEquals(100, GiftManager.getInstance().getGiftcfgList("203001").get(0).getWeight());

		Assert.assertEquals("[{\"count\":100,\"cfgId\":200002},{\"count\":6,\"cfgId\":213406},{\"count\":150,\"cfgId\":100001}]", VipManager.getInstance().getVipDataCfg(5).getRewardData());

		Assert.assertEquals("qiuyang", PropertiesLoader.getInstance().getString("qiunet"));

		Assert.assertEquals(20, MijingManager.getInstance().getMijingDataCfg(1, 3).getWeight());
	}
}
