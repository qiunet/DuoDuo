package org.qiunet.cfg.test.json;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.cfg.wrapper.INestListCfgWrapper;
import org.qiunet.cfg.wrapper.INestMapCfgWrapper;
import org.qiunet.cfg.wrapper.ISimpleMapCfgWrapper;
import org.qiunet.utils.scanner.ClassScanner;
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

	@BeforeClass
	public static void preExec() throws Throwable {
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void testReward(){
		JsonRewardInitCfg cfg = jsonRewardWrapper.getCfgById(1111);
		Assert.assertNotNull(cfg.getVal1());
		Assert.assertEquals(cfg.getVal1().size(), 3);

		Assert.assertEquals(Long.MAX_VALUE, cfg.getVal2());
	}
	@Test
	public void testNestList(){
		Assert.assertEquals(3, nestListCfgWrapper.size());
		Assert.assertTrue(nestListCfgWrapper.contains(1111));
		Assert.assertTrue(nestListCfgWrapper.contains(1111, 0));

		JsonNestListInitCfg cfg = nestListCfgWrapper.getCfgsById(2222, 0);
		Assert.assertEquals(cfg.getVal2(), 123457);
	}
	@Test
	public void testNestMap(){
		Assert.assertTrue(nestMapCfgWrapper.contains(1111, "1,2,3"));
		Assert.assertEquals(3, nestMapCfgWrapper.size());
		JsonNestMapInitCfg cfg = nestMapCfgWrapper.getCfgById(1111, "1,2,3");

		Assert.assertEquals(cfg.getVal2(), 123456);
	}
	@Test
	public void testSimpleMap() {
		JsonSimpleMapInitCfg cfg = simpleMapCfgWrapper.getCfgById(1111);
		Assert.assertEquals(3, simpleMapCfgWrapper.size());

		Assert.assertTrue(simpleMapCfgWrapper.contains(2222));
		Assert.assertTrue(simpleMapCfgWrapper.contains(3333));

		Assert.assertEquals(cfg.getVal1(), "1,2,3");
	}
}
