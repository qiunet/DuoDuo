package org.qiunet.data.core.mongo.pojo;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Primitives;
import org.bson.codecs.Codec;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.qiunet.data.core.mongo.IMongoEntity;
import org.qiunet.data.core.mongo.annotation.DbRef;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.*;
import java.util.*;

/***
 * 字段相关的数据
 * @author qiunet
 * 2024/2/27 11:14
 ***/
class DPropertyModel<T> {

	private final List<Type> parameters;

	private final boolean idField;

	private Codec<T> codec;

	private final Field field;
	/**
	 * 类型
	 */
	private final Class type;
	/**
	 * 是否是dbRef
	 */
	private boolean dbRef;
	/**
	 * 数据库中真实名
	 */
	private String dbFieldName;

	public DPropertyModel(Class<?> clazz, ParameterizedType parameterizedType, Field field) {
		Class<?> type1 = field.getType();
		if (TypeVariable.class.isAssignableFrom(field.getGenericType().getClass())) {
			TypeVariable genericType = (TypeVariable) field.getGenericType();
			TypeVariable<? extends Class<?>>[] variables = clazz.getTypeParameters();
			for (int i = 0; i < variables.length; i++) {
				if (Objects.equals(variables[i].getName(), genericType.getName())) {
					type1 = (Class<?>) parameterizedType.getActualTypeArguments()[i];
					break;
				}
			}
		}
		if (type1.isPrimitive()) {
			this.type = Primitives.wrap(type1);
		}else {
			this.type = type1;
		}

		boolean isAbstractOrInterface = Modifier.isAbstract(type.getModifiers()) || Modifier.isInterface(type.getModifiers());
		boolean isCollectionField = Collection.class.isAssignableFrom(type);
		boolean isMapField = Map.class.isAssignableFrom(type);
		if (! isMapField && !isCollectionField && ! type.isEnum() && isAbstractOrInterface) {
			throw new CustomException("Field {}#{} can not be abstract or interface!", field.getDeclaringClass().getName(), field.getName());
		}

		this.dbFieldName = field.getName();
		this.idField = field.isAnnotationPresent(BsonId.class);

		if (field.isAnnotationPresent(BsonProperty.class)) {
			this.dbFieldName = field.getAnnotation(BsonProperty.class).value();
		}

		if (this.idField) {
			this.dbFieldName = IMongoEntity.ID_FIELD_NAME;
		}

		this.dbRef = field.isAnnotationPresent(DbRef.class);
		if (isDbRef()) {
			Preconditions.checkState(IMongoEntity.class.isAssignableFrom(field.getType()), "DbRef must be a BasicMongoEntity object!");
		}

		if (ParameterizedType.class.isAssignableFrom(field.getGenericType().getClass())){
			this.parameters = List.of(((ParameterizedType) field.getGenericType()).getActualTypeArguments());
		}else {
			this.parameters = Collections.emptyList();
		}

		ReflectUtil.makeAccessible(field);
		this.field = field;
	}

	/**
	 * 获得值
	 * @param declareInstance field所属Class的对象值.
	 * @return
	 */
	public Object getPropertyVal(Object declareInstance) {
        try {
            return field.get(declareInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

	public List<Type> getParameters() {
		return parameters;
	}

	public String getDbFieldName() {
		return dbFieldName;
	}

	public Class<?> getType() {
		return this.type;
	}

	public Field getField() {
		return field;
	}

	public boolean isIdField() {
		return idField;
	}

	public boolean isDbRef() {
		return dbRef;
	}

	public Codec getCachedCodec(DPojoCodec pojoCodec) {
		if (this.codec == null) {
			this.codec = (Codec<T>) pojoCodec.registry.get(this.getType(), this.parameters);
		}
		return codec;
	}

	@Override
	public String toString() {
		return field.toString();
	}
}
