package org.qiunet.agent;

import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class JavaAgent {
	private static Object logger;
	private static Method method;
	private static Method emethod;
	static {
		try {
			Class clazz = Class.forName("org.qiunet.utils.logger.LoggerType");
			Field field = clazz.getField("DUODUO_HOTSWAP");
			logger = field.get(null);
			method = clazz.getMethod("error", String.class);
			emethod = clazz.getMethod("error", String.class, Throwable.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void logger(String message) {
		try {
			method.invoke(logger, message);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	private static void logger(String message, Throwable throwable) {
		try {
			emethod.invoke(logger, message, throwable);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void agentmain(String args, Instrumentation ins) {
		ClassInfos classInfos = ClassInfos.parse(args);

		logger("======开始热加载======");
		List<File> files = new ArrayList<>();
		List<ClassDefinition> classDefinitions = new ArrayList<>();
		for (String className : classInfos.getClassNames()) {
			className = className.replaceAll("\\/", ".");
			String[] strings = className.split("\\.");
			String fileName = strings[strings.length - 1];
			File file = Paths.get(classInfos.getClassPath(), fileName+".class").toFile();
			try {
				classDefinitions.add(new ClassDefinition(Class.forName(className), Files.readAllBytes(file.toPath())));
				files.add(file);
			} catch (Exception e) {
				logger("==HotSwap Fail!", e);
			}
		}
		if (classDefinitions.isEmpty()) {
			logger("没有需要加载的class");
			return;
		}

		try {
			ins.redefineClasses(classDefinitions.toArray(new ClassDefinition[classDefinitions.size()]));
			for (ClassDefinition classDefinition : classDefinitions) {
				logger("======热加载==["+classDefinition.getDefinitionClass().getName()+"]=成功===");
			}

			files.forEach(File::delete);
			logger("HotSwap Success! Delete files!!! ");
		} catch (Exception e) {
			logger("HotSwap Fail!", e);
		}finally {
			logger("======结束热加载======");
		}
	}
}
