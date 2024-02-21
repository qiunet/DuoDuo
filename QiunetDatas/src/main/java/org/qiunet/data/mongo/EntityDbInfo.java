package org.qiunet.data.mongo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.client.MongoCollection;
import org.qiunet.data.conf.ServerConfig;
import org.qiunet.data.enums.ServerType;
import org.qiunet.data.mongo.annotation.MongoDbEntity;
import org.qiunet.data.util.DbUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.string.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * Entity 的db信息
 * @author qiunet
 * 2024/2/29 14:36
 ***/
public class EntityDbInfo<E extends IMongoEntity<?>> {
	/**
	 * 每个IMongoEntity 的db信息缓存
	 */
	private static final Map<Class<? extends IMongoEntity<?>>, EntityDbInfo> entityDbInfos = Maps.newConcurrentMap();

	private final Class<E> clz;

	private final MongoDbClient client;
	/**
	 * 支持的server Type
	 */
	private final Set<ServerType> serverTypes;
	/**
	 * 表名
	 */
	private final String collectionName;
	/**
	 * db
	 */
	private final String db;

	public static <E extends IMongoEntity<?>> EntityDbInfo<E> get(Class<? extends IMongoEntity<?>> clz) {
		return entityDbInfos.computeIfAbsent(clz, EntityDbInfo::new);
	}

	private EntityDbInfo(Class<E> clz) {
		Preconditions.checkState(clz.isAnnotationPresent(MongoDbEntity.class), "Class [%s] not specify annotation @MongoDbEntity!", clz.getName());
		MongoDbEntity annotation = clz.getAnnotation(MongoDbEntity.class);
		this.client = MongoDbSupport.getClient(annotation.dbSource());
		String db = annotation.db();
		if (StringUtil.isEmpty(db)) {
			db = client.getDefaultDbName();
		}
		this.db = db;
		String collectionName = annotation.collection();
		if (StringUtil.isEmpty(collectionName)) {
			collectionName = DbUtil.getDefaultTableName(clz.getSimpleName());
		}
		List<ServerType> list = Arrays.asList(annotation.serverTypes());
		this.serverTypes = Sets.newEnumSet(list, ServerType.class);
		this.collectionName = collectionName;
		this.clz = clz;
	}

	/**
	 * 获得collection
	 * @return Mongodb collection
	 * @param <E>  IMongoEntity
	 */
	public <E> MongoCollection<E> getCollection() {
		if (! isValidServer()) {
			throw new CustomException("Can get mongodb execute in server!");
		}
		return (MongoCollection<E>) client.getCollection(this);
	}
	/**
	 * 是否支持该服务器类型
	 * @return true 支持
	 */
	public boolean isValidServer() {
		return this.serverTypes.contains(ServerConfig.getServerType());
	}

	public String getDb() {
		return db;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public Class<E> getClz() {
		return clz;
	}
}
