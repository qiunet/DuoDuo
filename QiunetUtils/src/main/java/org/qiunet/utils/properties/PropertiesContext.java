package org.qiunet.utils.properties;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.properties.anno.DProperties;
import org.qiunet.utils.properties.anno.DPropertiesValue;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 处理properties的context
 *
 * @author qiunet
 * 2020-09-18 10:17
 */
class PropertiesContext implements IApplicationContextAware {
	/**
	 * propertie名称对应的所有字段.
	 */
	private Map<String, List<Field>> fields = Maps.newHashMap();
	private IApplicationContext context;
	@Override
	public int order() {
		return Integer.MAX_VALUE - 1;
	}

	@Override
	public void setApplicationContext(IApplicationContext context) throws Exception {
		this.context = context;
		this.loadField();
	}

	/**
	 * 加载字段
	 */
	private void loadField(){
		Set<Field> fieldSet = this.context.getFieldsAnnotatedWith(DPropertiesValue.class);
		for (Field field : fieldSet) {
			DProperties annotation = field.getDeclaringClass().getAnnotation(DProperties.class);
			String propertiesName;
			if (annotation != null) {
				propertiesName = annotation.value();
			}else {
				DPropertiesValue fieldAnnotation = field.getAnnotation(DPropertiesValue.class);
				Preconditions.checkState(! StringUtil.isEmpty(fieldAnnotation.propertiesName()), "properties name is require!");
				propertiesName = fieldAnnotation.propertiesName();
			}

			List<Field> fieldList = this.fields.computeIfAbsent(propertiesName, key -> Lists.newArrayList());
			fieldList.add(field);
		}

		this.fields.keySet().forEach(this::loadFile);
	}


	/**
	 * 加载指定名文件
	 * @param name
	 */
	private void loadFile(String name) {
		IKeyValueData<Object, Object> keyValueData = PropertiesUtil.loadProperties(name, file -> this.loadFile(name, PropertiesUtil.loadProperties(file)));
		this.loadFile(name, keyValueData);
	}

	/**
	 * 加载文件
	 * @param name
	 * @param keyValueData
	 */
	private void loadFile(String name, IKeyValueData<Object, Object> keyValueData) {
		List<Field> fieldList = this.fields.get(name);
		fieldList.forEach(field -> {
			DPropertiesValue annotation = field.getAnnotation(DPropertiesValue.class);
			String keyName = annotation.value();
			if (StringUtil.isEmpty(keyName)) {
				keyName = field.getName();
			}
			Preconditions.checkState(keyValueData.containKey(keyName) || !StringUtil.isEmpty(annotation.defaultVal()), "Properties ["+name+"] do not have key ["+keyName+"], but field annotation defaultVal is empty!");
			String val = keyValueData.getString(keyName, annotation.defaultVal());
			Object instance = null;
			if (!Modifier.isStatic(field.getModifiers())) {
				instance = context.getInstanceOfClass(field.getDeclaringClass());
			}
			try {
				field.setAccessible(true);
				field.set(instance, this.convertVal(field.getType(), val));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 转换字段值
	 * @param fieldType
	 * @param val
	 * @return
	 */
	private Object convertVal(Class fieldType, String val) {
		try {
			Class<?> aClass = Class.forName("org.qiunet.cfg.convert.CfgFieldObjConvertManager");
			Method method = aClass.getMethod("covert", Class.class, String.class);
			return method.invoke(context.getInstanceOfClass(aClass), fieldType, val);
		} catch (ClassNotFoundException e) {
			if (fieldType == String.class) {
				return val;
			}

			if (fieldType == Integer.TYPE || fieldType == Integer.class) {
				return Integer.parseInt(val);
			}

			if (fieldType == Long.TYPE || fieldType == Long.class) {
				return Long.parseLong(val);
			}

			if (fieldType.isEnum() || Enum.class.isAssignableFrom(fieldType)) {
				return Enum.valueOf(fieldType, val);
			}
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Can not convert class type for ["+fieldType.getName()+"]");
	}
}
