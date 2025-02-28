package org.qiunet.data.core.mongo;

import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.concurrent.ExecutionException;

/***
 *
 * @author qiunet
 * 2024/2/22 15:29
 ***/
public class TestMongoDbSupport {
	private static final PlayerDataLoader playerDataLoader = PlayerDataLoader.get(1000123);

	@BeforeAll
	public static void init(){
		ClassScanner.getInstance(ScannerType.MONGODB).scanner();
	}

	@Test
	public void testMongodbEntity() {
		long playerId = MathUtil.random(1000000);
		long m1 = MathUtil.random(10000);
		long m2 = MathUtil.random(10000);
		CurrencyDo currencyDo = CurrencyDo.find(playerId);
		Assertions.assertNull(currencyDo);

		currencyDo = CurrencyDo.valueOf(playerId, "qiunet", m1, m2);
		currencyDo.save();

		currencyDo = CurrencyDo.find(playerId);
		Assertions.assertEquals(currencyDo.getId(), playerId);
		Assertions.assertEquals(currencyDo.getPurse(CurrencyType.M1), m1);
		Assertions.assertEquals(currencyDo.getPurse(CurrencyType.M2), m2);

		currencyDo.delete();
		currencyDo = CurrencyDo.find(playerId);
		Assertions.assertNull(currencyDo);
	}



	@Test
	public void testPlayerCacheEntity() throws InterruptedException, ExecutionException {
		long m1 = MathUtil.random(100000), m2 = MathUtil.random(1000000);
		long playerId = playerDataLoader.getPlayerId();
		GenderType type = GenderType.FEMALE;
		String name = "秋阳";
		int level = 104;

		PlayerDo playerDo = playerDataLoader.getEntity(PlayerDo.class);
		Assertions.assertNull(playerDo);

		playerDo = PlayerDo.valueOf(playerId, type, name, level, m1, m2);
		playerDo.getGlobalInfoDo().save();
		playerDataLoader.save(playerDo);

		playerDataLoader.syncToDb();

		PlayerDo entity = playerDataLoader.getEntity(PlayerDo.class);
		Assertions.assertEquals(entity, playerDo);
		// 失效
		playerDataLoader.dataCache.remove(PlayerDo.class);
		playerDo = playerDataLoader.getEntity(PlayerDo.class);
		Assertions.assertEquals(level, playerDo.getGlobalInfoDo().getLevel());
		Assertions.assertEquals(name, playerDo.getGlobalInfoDo().getName());

		playerDo.getGlobalInfoDo().setName("qiuyang");
		playerDo.getGlobalInfoDo().setLevel(100);
		playerDo.getGlobalInfoDo().save();
		playerDo.save();
		playerDataLoader.syncToDb();

		// 失效
		playerDataLoader.dataCache.remove(PlayerDo.class);

		playerDo = playerDataLoader.getEntity(PlayerDo.class);
		Assertions.assertEquals(playerDo.getGlobalInfoDo().getName(), "qiuyang");
		Assertions.assertEquals(playerDo.getGlobalInfoDo().getLevel(), 100);
		Assertions.assertEquals(playerDo.getGlobalInfoDo().getGender(), type);
		Assertions.assertEquals(playerDo.getPurse().getM1(), m1);
		Assertions.assertEquals(playerDo.getPurse().getM2(), m2);

		playerDo.delete();
		playerDo.getGlobalInfoDo().delete();

		playerDo = playerDataLoader.getEntity(PlayerDo.class);
		Assertions.assertNull(playerDo);
	}

	@Test
	public void testDocument() {
		BsonDocument bsonDocument = new BsonDocument();
		try (var write = new BsonDocumentWriter(bsonDocument)) {
			write.writeStartDocument();
			write.writeInt64("_id", 123456L);
			write.writeString("name", "qiunet");
			write.writeStartDocument("purge");
			write.writeInt64("M1", 11);
			write.writeInt64("M2", 22);
			write.writeEndDocument();
			write.writeEndDocument();
		}
		System.out.println(bsonDocument);
	}
}
