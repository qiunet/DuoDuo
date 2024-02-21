package org.qiunet.data.mongo.codec.pojo;

import com.mongodb.MongoClientSettings;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.qiunet.data.mongo.IMongoEntity;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

	private enum DatabaseInitialize implements IApplicationContextAware {
		instance;

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<? extends IMongoEntity>> classes = context.getSubTypesOf(IMongoEntity.class);
			for (Class<? extends IMongoEntity> aClass : classes) {
				if (Modifier.isInterface(aClass.getModifiers())
				 || Modifier.isAbstract(aClass.getModifiers())
				 || ! Modifier.isPublic(aClass.getModifiers())
				) {
					continue;
				}

				this.createClassModel(aClass);
			}
		}

		private void createClassModel(Class<?> clz) {
			if (DPojoCodecProvider.instance.classModels.containsKey(clz)) {
				return;
			}

			DClassModel<?> classModel = new DClassModel<>(clz);
			DPojoCodecProvider.instance.classModels.put(clz, classModel);
			Collection<DPropertyModel<?>> models = classModel.getPropertyModels();
			for (DPropertyModel<?> propertyModel : models) {
				if (Collection.class.isAssignableFrom(propertyModel.getType())
				 || Map.class.isAssignableFrom(propertyModel.getType())) {
					continue;
				}

				try {
					// Mongodb 基础类型  无须处理
					MongoClientSettings.getDefaultCodecRegistry().get(propertyModel.getType());
				}catch (Throwable e) {
					this.createClassModel(propertyModel.getType());
				}
			}
		}

		@Override
		public ScannerType scannerType() {
			return ScannerType.DATABASE;
		}
	}
}
