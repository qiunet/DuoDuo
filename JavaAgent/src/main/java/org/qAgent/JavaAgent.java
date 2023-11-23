package org.qAgent;


import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
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
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	private static void logger(String message, Throwable throwable) {
		try {
			emethod.invoke(logger, message, throwable);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void agentmain(String args, Instrumentation ins) {
		Path path = Paths.get(args);
		logger("======开始热加载======");
		logger("=======热加载目录: "+ path);
		List<ClassDefinition> classDefinitions = new ArrayList<>();
		List<File> fileList = new ArrayList<>();
		listFile(path, fileList);

		for (File file: fileList) {
			try {
				ClassFile classFile = new ClassFile(file);
				String className = classFile.getClassName();
				className = className.replaceAll("/", ".");

				logger("=======热加载Class名: "+className);

				byte[] bytes = Files.readAllBytes(file.toPath());
				Class<?> aClass;
				try {
					aClass = Class.forName(className);
				}catch (ClassNotFoundException e) {
					Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
					method.setAccessible(true);
					method.invoke(Thread.currentThread().getContextClassLoader(), className, bytes, 0, bytes.length);
					logger("======直接加载==["+className+"]=成功===");
					continue;
				}

				classDefinitions.add(new ClassDefinition(aClass, bytes));
			} catch (Exception e) {
				logger("==HotSwap Fail!", e);
			}
		}
		if (classDefinitions.isEmpty()) {
			logger("没有需要加载的class");
			return;
		}

		try {
			ins.redefineClasses(classDefinitions.toArray(new ClassDefinition[0]));
			for (ClassDefinition classDefinition : classDefinitions) {
				if (ins.isModifiableClass(classDefinition.getDefinitionClass())) {
					logger("======热加载==["+classDefinition.getDefinitionClass().getName()+"]=成功===");
				}else {
					logger("======热加载==["+classDefinition.getDefinitionClass().getName()+"]=失败===");
				}
			}

		} catch (Exception e) {
			logger("HotSwap Fail!", e);
		}finally {
			logger("======结束热加载======");
		}
	}

	private static void listFile(Path path, List<File> retList) {
		File[] files = path.toFile().listFiles();
		if (files == null) {
			return;
		}

		for (File file2 : files) {
			if (file2.isFile() && file2.getName().endsWith(".class")) {
				retList.add(file2);
				continue;
			}

			if (file2.isDirectory()) {
				listFile(file2.toPath(), retList);
			}
		}
	}
}
