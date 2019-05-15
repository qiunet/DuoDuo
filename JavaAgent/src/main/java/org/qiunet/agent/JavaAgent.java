package org.qiunet.agent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JavaAgent {

	public static void agentmain(String args, Instrumentation ins) {
		ClassInfos classInfos = ClassInfos.parse(args);

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
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (classDefinitions.isEmpty()) return;

		try {
			ins.redefineClasses(classDefinitions.toArray(new ClassDefinition[classDefinitions.size()]));
			files.forEach(File::delete);
			System.out.println("HotSwap Success!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnmodifiableClassException e) {
			e.printStackTrace();
		}
	}
}
