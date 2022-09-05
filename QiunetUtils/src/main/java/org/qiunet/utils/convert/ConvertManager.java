package org.qiunet.utils.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/***
 * 配置文件里面的字段对象转换器
 *
 * @author qiunet
 * 2020-02-04 13:09
 **/
public enum ConvertManager implements IApplicationContextAware {
	instance;
	/***
	 * 所有的convert
	 */
	private List<? extends BaseObjConvert> converts = Lists.newArrayList();
	private final AtomicBoolean inited = new AtomicBoolean(false);

	public static ConvertManager getInstance() {
		return instance;
	}

	private static final Map<Type, BaseObjConvert> convertMapping = Maps.newConcurrentMap();

	@Override
	public ScannerType scannerType() {
		return ScannerType.OBJ_CONVERT;
	}

	/**
	 * 转换并且set到字段.
	 * @param instance
	 * @param field
	 * @param val
	 */
	public void covertAndSet(Object instance, Field field, String val) {
		ReflectUtil.setField(instance, field, this.convert(field, val));
	}
	/***
	 * 按照指定的class 类型转换str
	 * @param field
	 * @param val
	 * @return 没有转换器将抛出异常
	 */
	public Object convert(Field field, String val) {
		if (! inited.get()) {
			throw new CustomException("ConvertManager not init");
		}
		BaseObjConvert<?> objConvert = convertMapping.computeIfAbsent(field.getGenericType(), clz -> {
				for (BaseObjConvert<?> convert : converts) {
					if (convert.canConvert(field)) {
						return convert;
					}
				}
				return null;
			});

		if (objConvert != null) {
			return objConvert.fromString(field, val);
		}

		if (StringUtil.isEmpty(val)) {
			return null;
		}

		// json 转换.
		if ((val.startsWith("{") && val.endsWith("}")) || (val.startsWith("[") && val.endsWith("]"))) {
			return JsonUtil.getGeneralObj(val, field.getGenericType());
		}

		throw new CustomException("Can not convert class type for field[{}] value[{}]", field.getName(), val);
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) {
		if (! inited.compareAndSet(false, true)) {
			return;
		}

		this.converts = context.getSubTypesOf(BaseObjConvert.class).stream()
			.filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
			.map(clazz -> (BaseObjConvert)context.getInstanceOfClass(clazz))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		LoggerType.DUODUO_CFG_READER.debug("find {} cfg field convert!", this.converts.size());
	}

	public List<? extends BaseObjConvert> getConverts() {
		return converts;
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}
}
