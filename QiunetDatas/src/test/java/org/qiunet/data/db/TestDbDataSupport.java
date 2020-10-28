package org.qiunet.data.db;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.data.support.DbDataListSupport;
import org.qiunet.data.support.DbDataSupport;
import org.qiunet.utils.scanner.ClassScanner;

import java.util.Map;

public class TestDbDataSupport {

	private static DbDataSupport<Long, PlayerDo, PlayerBo> dataSupport = new DbDataSupport<>(PlayerDo.class, PlayerBo::new);

	private static DbDataListSupport<Long, Integer, ItemDo, ItemBo> dataListSupport = new DbDataListSupport<>(ItemDo.class, ItemBo::new);
	private long uid = 10000;

	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void testEntity() {
		String name = "秋阳";
		PlayerDo playerDo = new PlayerDo();
		playerDo.setExp(1111111111111L);
		playerDo.setName("秋阳1");
		playerDo.setLevel(10);
		playerDo.setUid(uid);
		PlayerBo playerBo = playerDo.insert();

		playerBo.getDo().setName(name);
		playerBo.getDo().setLevel(100);
		playerBo.update();

		PlayerBo bo = dataSupport.getBo(uid);
		Assert.assertEquals(bo.getDo().getName(), name);
		Assert.assertEquals(bo.getDo().getLevel(), 100);

		bo.delete();

		bo = dataSupport.getBo(uid);
		Assert.assertNull(bo);
	}

	@Test
	public void testEntityList(){

		ItemDo itemDo1 = new ItemDo();
		itemDo1.setUid(uid);
		itemDo1.setItem_id(1);
		itemDo1.setCount(1);
		ItemBo bo1 = itemDo1.insert();

		ItemDo itemDo2 = new ItemDo();
		itemDo2.setUid(uid);
		itemDo2.setItem_id(2);
		itemDo2.setCount(2);
		ItemBo bo2 = itemDo2.insert();

		Map<Integer, ItemBo> boMap = dataListSupport.getBoMap(uid);
		Assert.assertEquals(boMap.size(), 2);

		for (ItemBo bo : boMap.values()) {
			Assert.assertTrue(bo.getDo().getItem_id() >= 1 && bo.getDo().getItem_id() <= 2);
			Assert.assertEquals(bo.getDo().getCount(), bo.getDo().getItem_id());
		}

		bo2.getDo().setCount(3);
		bo2.update();

		boMap = dataListSupport.getBoMap(uid);
		boMap.values().forEach(po -> {
			if (po.getDo().getItem_id() == 2){
				Assert.assertEquals(3, po.getDo().getCount());
			}
		});

		boMap.values().forEach(ItemBo::delete);

		boMap = dataListSupport.getBoMap(uid);
		Assert.assertTrue(boMap.isEmpty());
	}
}
