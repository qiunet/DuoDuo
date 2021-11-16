package org.qiunet.utils.scanner;

import com.google.common.collect.Lists;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.anno.AutoWired;
import org.qiunet.utils.scanner.anno.IgnoreEmptyWired;
import org.qiunet.utils.scanner.event.AutoWireCompleteEventData;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/***
 * 实例自动注入
 *
 * @author qiunet
 * 2020-12-28 12:45
 */
 enum AutoWiredHandler implements IApplicationContextAware {
	instance;
	private IApplicationContext context;
 	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
 		this.context = context;
		Set<Field> fields = context.getFieldsAnnotatedWith(AutoWired.class);
		for (Field field : fields) {
			if (this.handlerCfgAutoWire(field)) {
				continue;
			}
			Class<?> fieldType = field.getType();
			Class<?> type = fieldType;
			if (Modifier.isAbstract(fieldType.getModifiers())
			 || Modifier.isInterface(fieldType.getModifiers())) {
				Set<Class<?>> classes = context.getSubTypesOf((Class<Object>) fieldType);
				if (classes.isEmpty()) {
					if (! fieldType.isAnnotationPresent(IgnoreEmptyWired.class)) {
						throw new CustomException("Field type {} have none subType class, Do not know how to wired", fieldType.getName());
					}
					continue;
				}

				if (classes.size() > 1) {
					throw new CustomException("Field type {} have much subType class, Do not know how to wired", fieldType.getName());
				}

				type = Lists.newArrayList(classes).get(0);
			}
			// 被注入的对象
			Object autoWiredObj = context.getInstanceOfClass(type);
			Object declaringObj = null;
			if (! Modifier.isStatic(field.getModifiers())) {
				declaringObj = context.getInstanceOfClass(field.getDeclaringClass());
			}
			ReflectUtil.setField(declaringObj, field, autoWiredObj);
		}
		AutoWireCompleteEventData.instance.fireEventHandler();
	}

	private static final LazyLoader<Class<?>> cfgScannerManagerClass = new LazyLoader<>(() -> {
		String cfgAutoWireClass = "org.qiunet.cfg.annotation.support.CfgScannerManager";
		try {
			return Class.forName(cfgAutoWireClass);
		}catch (Exception e) {
			return null;
		}
	});
	private boolean handlerCfgAutoWire(Field field) throws Exception {
		if (cfgScannerManagerClass.get() == null) {
			return false;
		}
		Object cfgScannerManager = this.context.getInstanceOfClass(cfgScannerManagerClass.get());
		Method method = cfgScannerManagerClass.get().getDeclaredMethod("cfgAutoWired", Field.class);
		method.setAccessible(true);
		Boolean result = (Boolean) method.invoke(cfgScannerManager, field);
		return result;
	}
}
