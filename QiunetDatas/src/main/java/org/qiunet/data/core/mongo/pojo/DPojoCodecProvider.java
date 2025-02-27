package org.qiunet.data.core.mongo.pojo;

import com.google.common.collect.Lists;
import com.mongodb.MongoClientSettings;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.qiunet.data.core.mongo.EntityDbInfo;
import org.qiunet.data.core.mongo.IMongoEntity;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/***
 * 自己的解析pojo的codec provider
 * 官方的必须有get set方法或者是public.
 * 有点不方便
 *
 * @author qiunet
 * 2024/2/27 11:07
 ***/
public enum DPojoCodecProvider implements CodecProvider {
	instance;

	final Map<Class<?>, DClassModel<?>> classModels = new HashMap<>();

	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		DClassModel<?> model = classModels.get(clazz);
		if (model == null) {
			return null;
		}
		return new DPojoCodec(this, registry, model);
	}

	enum DatabaseInitialize implements IApplicationContextAware {
		instance;

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<? extends IMongoEntity>> classes = context.getSubTypesOf(IMongoEntity.class);
			Set<String> collectionNames = new HashSet<>();
			for (Class<? extends IMongoEntity> aClass : classes) {
				if (Modifier.isInterface(aClass.getModifiers())
				 || Modifier.isAbstract(aClass.getModifiers())
				 || ! Modifier.isPublic(aClass.getModifiers())
				) {
					continue;
				}

				EntityDbInfo<IMongoEntity<?>> info = EntityDbInfo.get((Class<? extends IMongoEntity<?>>) aClass);
				if (collectionNames.contains(info.getCollectionName())) {
					throw new CustomException("collection name ["+info.getCollectionName()+"] is duplicate.");
				}

				this.createClassModel(aClass);

				DClassModel<?> model = DPojoCodecProvider.instance.classModels.get(aClass);
				boolean haveIdField = model.getPropertyModels().stream().anyMatch(DPropertyModel::isIdField);
				if (! haveIdField) {
					throw new CustomException("IMongoEntity ["+aClass.getName()+"] not have field with @BsonId.");
				}
			}
		}

		void createClassModel(Class<?> clz) {
			if (DPojoCodecProvider.instance.classModels.containsKey(clz)) {
				return;
			}

			DClassModel<?> classModel = new DClassModel<>(clz);
			DPojoCodecProvider.instance.classModels.put(clz, classModel);
			Collection<DPropertyModel<?>> models = classModel.getPropertyModels();
			for (DPropertyModel<?> propertyModel : models) {
				if (Collection.class.isAssignableFrom(propertyModel.getType())
					|| Map.class.isAssignableFrom(propertyModel.getType())) {
					this.handlerMapAndListParameter(propertyModel, propertyModel.getParameters());
					continue;
				}

				this.fieldTypeDefineCheck(propertyModel, propertyModel.getType());
				this.touchPropertyModel(propertyModel.getType());
			}
		}

		private void handlerMapAndListParameter(DPropertyModel<?> propertyModel, List<Type> types) {
			for (Type parameter : types) {
				if (ParameterizedType.class.isAssignableFrom(parameter.getClass())) {
					this.handlerMapAndListParameter(propertyModel, Arrays.asList(((ParameterizedType) parameter).getActualTypeArguments()));
					this.handlerMapAndListParameter(propertyModel, Lists.newArrayList(((ParameterizedType) parameter).getRawType()));
					continue;
				}

				this.fieldTypeDefineCheck(propertyModel, (Class<?>) parameter);
				this.touchPropertyModel((Class<?>) parameter);
			}
		}

		private void fieldTypeDefineCheck(DPropertyModel<?> propertyModel, Class<?> type) {
			if (type == Object.class) {
				// 可能有未知类型并且不可解析,造成无法热更问题.
				throw new CustomException("Field [{}#{}] define with Object type, maybe take UnknownTypeException!", propertyModel.getField().getDeclaringClass().getName(), propertyModel.getField().getName());
			}
		}

		private void touchPropertyModel(Class<?> type) {
			try {
				// Mongodb 基础类型  无须处理
				MongoClientSettings.getDefaultCodecRegistry().get(type);
			}catch (Throwable e) {
				this.createClassModel(type);
			}
		}
		@Override
		public ScannerType scannerType() {
			return ScannerType.MONGODB;
		}

		@Override
		public int order() {
			return Integer.MAX_VALUE - 1;
		}
	}
}
