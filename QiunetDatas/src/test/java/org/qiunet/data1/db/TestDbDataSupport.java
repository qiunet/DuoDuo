package org.qiunet.data1.db;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data1.support.DbDataListSupport;
import org.qiunet.data1.support.DbDataSupport;

import java.util.Map;

public class TestDbDataSupport {

	private static DbDataSupport<Long, PlayerPo, PlayerBo> dataSupport = new DbDataSupport<>(PlayerPo.class, PlayerBo::new);

	private static DbDataListSupport<Long, Integer, ItemPo, ItemBo> dataListSupport = new DbDataListSupport<>(ItemPo.class, ItemBo::new);
	private long uid = 10000;

	@Test
	public void testEntity() {
		String name = "秋阳";
		PlayerPo playerPo = new PlayerPo();
		playerPo.setExp(1111111111111L);
		playerPo.setName("秋阳1");
		playerPo.setLevel(10);
		playerPo.setUid(uid);
		PlayerBo playerBo = playerPo.insert();

		playerBo.getPo().setName(name);
		playerBo.getPo().setLevel(100);
		playerBo.update();

		PlayerBo bo = dataSupport.getBo(uid);
		Assert.assertEquals(bo.getPo().getName(), name);
		Assert.assertEquals(bo.getPo().getLevel(), 100);

		bo.delete();

		bo = dataSupport.getBo(uid);
		Assert.assertNull(bo);
	}

	@Test
	public void testEntityList(){

		ItemPo itemPo1 = new ItemPo();
		itemPo1.setUid(uid);
		itemPo1.setItem_id(1);
		itemPo1.setCount(1);
		ItemBo bo1 = itemPo1.insert();

		ItemPo itemPo2 = new ItemPo();
		itemPo2.setUid(uid);
		itemPo2.setItem_id(2);
		itemPo2.setCount(2);
		ItemBo bo2 = itemPo2.insert();

		Map<Integer, ItemBo> boMap = dataListSupport.getBoMap(uid);
		Assert.assertEquals(boMap.size(), 2);

		for (ItemBo bo : boMap.values()) {
			Assert.assertTrue(bo.getPo().getItem_id() >= 1 && bo.getPo().getItem_id() <= 2);
			Assert.assertEquals(bo.getPo().getCount(), bo.getPo().getItem_id());
		}

		bo2.getPo().setCount(3);
		bo2.update();

		boMap = dataListSupport.getBoMap(uid);
		boMap.values().forEach(po -> {
			if (po.getPo().getItem_id() == 2){
				Assert.assertEquals(3, po.getPo().getCount());
			}
		});

		boMap.values().forEach(ItemBo::delete);

		boMap = dataListSupport.getBoMap(uid);
		Assert.assertTrue(boMap.isEmpty());
	}
}
