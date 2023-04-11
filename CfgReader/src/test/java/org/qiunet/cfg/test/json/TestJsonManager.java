package org.qiunet.cfg.test.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.cfg.manager.base.INestListCfgWrapper;
import org.qiunet.cfg.manager.base.INestMapCfgWrapper;
import org.qiunet.cfg.manager.base.ISimpleMapCfgWrapper;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.scanner.anno.AutoWired;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 18:26
 ***/
public class TestJsonManager {
	@AutoWired
	private static ISimpleMapCfgWrapper<Integer, JsonSimpleMapInitCfg> simpleMapCfgWrapper;
	@AutoWired
	private static ISimpleMapCfgWrapper<Integer, JsonRewardInitCfg> jsonRewardWrapper;
	@AutoWired
	private static INestMapCfgWrapper<Integer, String, JsonNestMapInitCfg> nestMapCfgWrapper;
	@AutoWired
	private static INestListCfgWrapper<Integer, JsonNestListInitCfg> nestListCfgWrapper;

	@BeforeAll
	public static void preExec() throws Throwable {
		ClassScanner.getInstance(ScannerType.CFG).scanner();
	}

	@Test
	public void testReward(){
		JsonRewardInitCfg cfg = jsonRewardWrapper.getCfgById(1111);
		Assertions.assertNotNull(cfg.getVal1());
		Assertions.assertEquals(cfg.getVal1().size(), 3);

		Assertions.assertEquals(Long.MAX_VALUE, cfg.getVal2());
	}
	@Test
	public void testNestList(){
		Assertions.assertEquals(3, nestListCfgWrapper.size());
		Assertions.assertTrue(nestListCfgWrapper.contains(1111));
		Assertions.assertTrue(nestListCfgWrapper.contains(1111, 0));

		JsonNestListInitCfg cfg = nestListCfgWrapper.getCfgsById(2222, 0);
		Assertions.assertEquals(cfg.getVal2(), 123457);
	}
	@Test
	public void testNestMap(){
		Assertions.assertTrue(nestMapCfgWrapper.contains(1111, "1;2;3"));
		Assertions.assertEquals(3, nestMapCfgWrapper.size());
		JsonNestMapInitCfg cfg = nestMapCfgWrapper.getCfgById(1111, "1;2;3");

		Assertions.assertEquals(cfg.getVal2(), 123456);
	}
	@Test
	public void testSimpleMap() {
		JsonSimpleMapInitCfg cfg = simpleMapCfgWrapper.getCfgById(1111);
		Assertions.assertEquals(3, simpleMapCfgWrapper.size());

		Assertions.assertTrue(simpleMapCfgWrapper.contains(2222));
		Assertions.assertTrue(simpleMapCfgWrapper.contains(3333));

		Assertions.assertEquals(cfg.getVal1(), "1;2;3");
	}
}
