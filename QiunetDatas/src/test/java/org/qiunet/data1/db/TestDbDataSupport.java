package org.qiunet.data1.db;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data1.db.entity.DbEntity;
import org.qiunet.data1.support.DbDataListSupport;
import org.qiunet.data1.support.DbDataSupport;

import java.util.List;

public class TestDbDataSupport {

	private static DbDataSupport<Long, PlayerPo> dataSupport = new DbDataSupport<>(PlayerPo.class);

	private static DbDataListSupport<Long, Integer, ItemPo> dataListSupport = new DbDataListSupport<>(ItemPo.class);
	private long uid = 10000;

	@Test
	public void testEntity() {
		String name = "秋阳";
		PlayerPo playerPo = new PlayerPo();
		playerPo.setExp(1111111111111L);
		playerPo.setName("秋阳1");
		playerPo.setLevel(10);
		playerPo.setUid(uid);
		playerPo.insert();

		playerPo.setName(name);
		playerPo.setLevel(100);
		playerPo.update();

		playerPo = dataSupport.getPo(uid);
		Assert.assertEquals(playerPo.getName(), name);
		Assert.assertEquals(playerPo.getLevel(), 100);

		playerPo.delete();
		playerPo = dataSupport.getPo(uid);
		Assert.assertNull(playerPo);
	}

	@Test
	public void testEntityList(){

		ItemPo itemPo1 = new ItemPo();
		itemPo1.setUid(uid);
		itemPo1.setItem_id(1);
		itemPo1.setCount(1);
		itemPo1.insert();

		ItemPo itemPo2 = new ItemPo();
		itemPo2.setUid(uid);
		itemPo2.setItem_id(2);
		itemPo2.setCount(2);
		itemPo2.insert();

		List<ItemPo> poList = dataListSupport.getPoList(uid);
		Assert.assertEquals(poList.size(), 2);

		for (ItemPo itemPo : poList) {
			Assert.assertTrue(itemPo.getItem_id() >= 1 && itemPo.getItem_id() <= 2);
			Assert.assertEquals(itemPo.getCount(), itemPo.getItem_id());
		}

		itemPo2.setCount(3);
		itemPo2.update();

		poList = dataListSupport.getPoList(uid);
		poList.forEach(po -> {
			if (po.getItem_id() == 2){
				Assert.assertEquals(3, po.getCount());
			}
		});

		poList.forEach(ItemPo::delete);

		poList = dataListSupport.getPoList(uid);
		Assert.assertTrue(poList.isEmpty());
	}
}
