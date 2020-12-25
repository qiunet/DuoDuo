package org.qiunet.utils.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.collection.generics.StringSet;
import org.qiunet.utils.config.anno.DConfig;
import org.qiunet.utils.config.anno.DConfigValue;
import org.qiunet.utils.config.hocon.DHocon;
import org.qiunet.utils.config.properties.DProperties;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 处理properties的context
 *
 * @author qiunet
 * 2020-09-18 10:17
 */
enum ConfigContext implements IApplicationContextAware {
	instance;

	/**
	 * propertie名称对应的所有字段.
	 */
	private final Map<String, ConfigData> datas = Maps.newHashMap();
	private IApplicationContext context;
	/**
	 * reflections bug. 必须有定义. 才不会抛出异常.
	 */
	@DConfigValue
	private String field_holder;
	private Field field_holder_field;
	ConfigContext() {
		try {
			field_holder_field = ConfigContext.class.getDeclaredField("field_holder");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	@Override
	public int order() {
		return Integer.MAX_VALUE - 1;
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.context = context;
		this.loadField();
	}

	/**
	 * 加载字段
	 */
	private void loadField(){
		Set<Field> fieldSet = this.context.getFieldsAnnotatedWith(DConfigValue.class);
		for (Field field : fieldSet) {
			if (field.equals(field_holder_field)) {
				continue;
			}

			DConfig annotation = field.getDeclaringClass().getAnnotation(DConfig.class);
			String configName;
			if (annotation != null) {
				configName = annotation.value();
			}else {
				DConfigValue fieldAnnotation = field.getAnnotation(DConfigValue.class);
				Preconditions.checkState(! StringUtil.isEmpty(fieldAnnotation.configName()), "config name is require!");
				configName = fieldAnnotation.configName();
			}

			ConfigData data = this.datas.computeIfAbsent(configName, ConfigData::new);
			if (annotation != null) data.listenerChanged = annotation.listenerChange();
			data.fields.add(field);
		}

		this.datas.values().forEach(this::loadFile);
	}


	/**
	 * 加载指定名文件
	 * @param data
	 */
	private void loadFile(ConfigData data) {
		this.loadFile(data.configName, data.fields, data.loadData(keyVal -> {
			this.loadFile(data.configName, data.fields, keyVal);
		}));
	}

	/**
	 * 加载文件
	 * @param name
	 * @param keyValueData
	 */
	private void loadFile(String name, List<Field> fieldList, IKeyValueData<String, String> keyValueData) {
		fieldList.forEach(field -> {
			DConfigValue annotation = field.getAnnotation(DConfigValue.class);
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

			if (fieldType == Boolean.TYPE || fieldType == Boolean.class) {
				return Boolean.getBoolean(val);
			}

			if (fieldType == StringSet.class) {
				return new StringSet(Arrays.asList(StringUtil.split(val, ",")));
			}

			if (fieldType.isEnum() || Enum.class.isAssignableFrom(fieldType)) {
				return Enum.valueOf(fieldType, val);
			}


		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		throw new CustomException("Can not convert class type for ["+fieldType.getName()+"]");
	}

	/**
	 * properties 数据
	 */
	private static class ConfigData {
		/**
		 * 名称
		 */
		 String configName;
		/**
		 * 是否监听
		 */
		 boolean listenerChanged;
		/**
		 * 需要注入的field
		 */
		 List<Field> fields = Lists.newLinkedList();

		ConfigData(String configName) {
			this.configName = configName;
		}

		IKeyValueData<String, String> loadData(IKeyValueData.DataChangeListener<String, String> changeListener){
			if (! listenerChanged) {
				changeListener = null;
			}
			if (configName.endsWith(".properties")) {
				return new DProperties(configName, changeListener);
			}else if (configName.endsWith(".conf")) {
				return new DHocon(configName, changeListener);
			}else {
				throw new CustomException("Not support config for [{}]", configName);
			}
		}
	}
}
