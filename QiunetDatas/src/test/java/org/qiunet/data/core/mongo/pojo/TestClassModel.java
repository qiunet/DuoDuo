package org.qiunet.data.core.mongo.pojo;

import com.google.common.collect.Lists;
import com.mongodb.client.model.Filters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.data.core.mongo.IMongoEntity;
import org.qiunet.data.core.mongo.MongoDbSupport;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2024/9/6 11:07
 ***/
public class TestClassModel {

	@Test()
	public void testMapList() {
		Assertions.assertDoesNotThrow(() ->
			DPojoCodecProvider.DatabaseInitialize.instance.createClassModel(PojoDataDo.class)
		);
	}

	@Test
	public void testMapMap() {
		Assertions.assertDoesNotThrow(() ->
			DPojoCodecProvider.DatabaseInitialize.instance.createClassModel(PojoDataV2Do.class)
		);
	}

	@Test
	public void test2MapReadWrite() {
		ClassScanner.getInstance(ScannerType.MONGODB, ScannerType.EVENT).scanner();
		long playerId = 1;
		Map<Integer, List<String>> map = new HashMap<>();
		map.computeIfAbsent(1, k -> Lists.newArrayList("1", "2"));
		map.computeIfAbsent(2, k -> Lists.newArrayList("3", "4"));
		PojoDataDo pojoDataDo = PojoDataDo.valueOf(playerId, map);
		Assertions.assertDoesNotThrow(() -> {
			pojoDataDo.save();
		});
	}

	@Test
	public void testMapListReadWrite() {
		ClassScanner.getInstance(ScannerType.MONGODB).scanner();
		long playerId = 1;
		Map<Integer, List<String>> map = new HashMap<>();
		map.computeIfAbsent(1, k -> Lists.newArrayList("1", "2"));
		map.computeIfAbsent(2, k -> Lists.newArrayList("3", "4"));
		PojoDataDo pojoDataDo = PojoDataDo.valueOf(playerId, map);
		Assertions.assertDoesNotThrow(() -> {
			pojoDataDo.save();
		});

		PojoDataDo dataDo = IMongoEntity._find(PojoDataDo.class, playerId);
		Assertions.assertNotNull(dataDo);
		Assertions.assertEquals(dataDo.getId(), playerId);
		Assertions.assertEquals(dataDo.getMap().size(), map.size());
		Assertions.assertEquals(dataDo.getMap().get(1).size(), map.get(1).size());
		Assertions.assertTrue(dataDo.getMap().get(1).contains("1"));
		Assertions.assertTrue(dataDo.getMap().get(1).contains("2"));
		Assertions.assertTrue(dataDo.getMap().get(2).contains("3"));
		Assertions.assertTrue(dataDo.getMap().get(2).contains("4"));
		MongoDbSupport.getDocCollection(PojoDataDo.class).deleteOne(Filters.eq(IMongoEntity.ID_FIELD_NAME, playerId));
	}
}
