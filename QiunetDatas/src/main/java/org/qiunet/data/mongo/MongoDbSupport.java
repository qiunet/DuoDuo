package org.qiunet.data.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.qiunet.data.conf.ServerConfig;
import org.qiunet.data.mongo.codec.pojo.DPojoCodecProvider;
import org.qiunet.data.mongo.provide.DMapCodecProvider;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;

import java.util.HashMap;
import java.util.Map;


/***
 * url里面的database 为默认db. 如果Entity 没有指定Db注解. 则使用默认的db.
 * url 示例: mongodb://username:password@192.168.2.63:3612/?authSource=admin&minPoolSize=4&maxPoolSize=16&maxIdleTimeMS=920000
 * @author qiunet
 * 2023/8/22 17:54
 */
public final class MongoDbSupport {

	/**
	 * 保存dbSource 对应的 Client
	 */
	private static final Map<String, MongoDbClient> clientMap = new HashMap<>();
	/**
	 * server 配置
	 */
	private static final ServerConfig serverConfig = ServerConfig.instance;

	/**
	 * 获得指定dbSource的client
	 *
	 * @param clz 获取指定Clz的Collection
	 * @return MongoCollection
	 */
	public static <Key, Entity extends IMongoEntity<Key>> MongoCollection<Entity> getCollection(Class<Entity> clz) {
		EntityDbInfo<Entity> entityDbInfo = EntityDbInfo.get(clz);
		return entityDbInfo.getCollection();
	}
	/**
	 * 获得指定dbSource的client
	 *
	 * @param clz 获取指定Clz的 Document Collection
	 * @return MongoCollection
	 */
	public static <Key, Entity extends IMongoEntity<Key>> MongoCollection<Document> getDocCollection(Class<Entity> clz) {
		EntityDbInfo<Entity> entityDbInfo = EntityDbInfo.get(clz);
		return entityDbInfo.getDocCollection();
	}

	static MongoDbClient getClient(String dbSource) {
		return clientMap.computeIfAbsent(dbSource, MongoDbSupport::createMongodbClient);
	}

	/**
	 * 创建MongodbClient
	 *
	 * @param dbSource db source name
	 * @return Mongodb client
	 */
	private static MongoDbClient createMongodbClient(String dbSource) {
		ConnectionString connectionString = new ConnectionString(serverConfig.getString(buildConfigKey(dbSource)));
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(DPojoCodecProvider.instance, new DMapCodecProvider());
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(pojoCodecRegistry, MongoClientSettings.getDefaultCodecRegistry());
		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
			.addCommandListener(MongoDbCommandListener.instance)
			.codecRegistry(codecRegistry)
			.build();

		return new MongoDbClient(clientSettings, connectionString);

	}

	/**
	 * 构造配置参数
	 * @param dbSource source
	 * @return
	 */
	private static String buildConfigKey(String dbSource) {
		return "mongo." +dbSource+ ".url";
	}
	/**
	 * 关闭
	 */
	@EventListener
	private void shutdown(ServerShutdownEvent event) {
		for (MongoDbClient client : clientMap.values()) {
			client.close();
		}
	}
}
