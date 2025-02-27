package org.qiunet.data.core.mongo.pojo;

import com.mongodb.DBRef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.qiunet.data.core.mongo.EntityDbInfo;
import org.qiunet.data.core.mongo.IMongoEntity;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.exceptions.EnumParseException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;

import java.util.Objects;

/***
 *
 * @author qiunet
 * 2024/2/27 11:56
 ***/
class DPojoCodec<T> implements Codec<T> {

	final DPojoCodecProvider pojoCodecProvider;

	final CodecRegistry registry;

	private final DClassModel<T> model;

	public DPojoCodec(DPojoCodecProvider pojoCodecProvider, CodecRegistry registry, DClassModel<T> model) {
		this.pojoCodecProvider = pojoCodecProvider;
		this.registry = registry;
		this.model = model;
	}

	@Override
	public T decode(BsonReader reader, DecoderContext decoderContext) {
		reader.readStartDocument();
		Object instance = model.createInstance();
		while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			String name = reader.readName();
			DPropertyModel<?> property = model.getProperty(name);
			if (property == null) {
				if (LoggerType.DUODUO_SQL.isDebugEnabled()) {
					LoggerType.DUODUO_SQL.debug("No such field {} in Class {}", name, model.getClz().getName());
				}
				reader.skipValue();
				continue;
			}
			Object fieldObj;
			if (property.isDbRef()){
				MongoCollection<IMongoEntity<?>> collection = EntityDbInfo.get((Class<? extends IMongoEntity<?>>) property.getType()).getCollection();
				fieldObj = collection.find(Filters.eq(IMongoEntity.ID_FIELD_NAME, this.readDbRefId(reader))).first();
			}else {
				fieldObj = readPropertyValue(property, reader, decoderContext);
			}
			if (fieldObj != null) {
				ReflectUtil.setField(instance, property.getField(), fieldObj);
			}
		}

		reader.readEndDocument();
		return (T) instance;
	}

	private Object readPropertyValue(DPropertyModel<?> property, BsonReader reader, DecoderContext decoderContext) {
		if (reader.getCurrentBsonType() == BsonType.NULL) {
			reader.readNull();
			return null;
		}

		Codec codec = property.getCachedCodec(this);
		return decoderContext.decodeWithChildContext(codec, reader);
	}


	@Override
	public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
		writer.writeStartDocument();
		for (DPropertyModel<?> propertyModel : model.getPropertyModels()) {
			this.encodeProperty(writer, propertyModel, value, encoderContext);
		}
		writer.writeEndDocument();
	}

	private void encodeProperty(BsonWriter writer, DPropertyModel<?> model, T value, EncoderContext encoderContext) {
		writer.writeName(model.getDbFieldName());
		Object propertyValue = model.getPropertyVal(value);
		if (model.isDbRef()) {
			EntityDbInfo entityDbInfo = EntityDbInfo.get((Class<? extends IMongoEntity<?>>) model.getType());
			DBRef dbRef = new DBRef(entityDbInfo.getDb(), entityDbInfo.getCollectionName(), ((IMongoEntity) propertyValue).getId());
			Codec<DBRef> dbRefCodec = registry.get(DBRef.class);
			dbRefCodec.encode(writer, dbRef, encoderContext);
		}else {
			encoderContext.encodeWithChildContext(model.getCachedCodec(this), writer, propertyValue);
		}
	}

	@Override
	public Class<T> getEncoderClass() {
		return this.model.getClz();
	}

	/**
	 * 读取dbRef 的id字段
	 * @param reader
	 * @return
	 */
	private Object readDbRefId(BsonReader reader) {
		Object id = null;
		reader.readStartDocument();
		while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			String name = reader.readName();
			DbRefField field = DbRefField.parse(name);
			if (field != DbRefField.ID) {
				field.readValue(reader);
				continue;
			}
			id = field.readValue(reader);
		}
		reader.readEndDocument();
		return id;
	}

	private enum DbRefField {
		REF("$ref"),
		DB("$db"),
		ID("$id") {
			@Override
			public Object readValue(BsonReader reader) {
				return switch (reader.getCurrentBsonType()) {
					case INT32 -> reader.readInt32();
					case INT64 -> reader.readInt64();
					case STRING -> reader.readString();
					case BOOLEAN -> reader.readBoolean();
					default -> throw new CustomException("Not support for "+reader.getCurrentBsonType());
				};
			}
		},
		;
		private final String name;

		DbRefField(String name) {
			this.name = name;
		}

		public Object readValue(BsonReader reader) {
			return reader.readString();
		}

		private static DbRefField [] values = values();
		public static DbRefField parse(String fieldName) {
			for (DbRefField value : values) {
				if (Objects.equals(value.name, fieldName)) {
					return value;
				}
			}
			throw new EnumParseException(fieldName);
		}
	}
}
