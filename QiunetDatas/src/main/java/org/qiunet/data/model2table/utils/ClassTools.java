package org.qiunet.data.model2table.utils;

import org.qiunet.data.model2table.annotation.InitData;
import org.qiunet.data.model2table.annotation.Table;
import org.qiunet.data.model2table.config.ConfigLoder;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * 通过包名获取class
 *
 * @author sunchenbin
 * @version 2016年6月23日 下午5:55:18
 */
public class ClassTools {

	public static Set<Class<?>> scanClasses(String packName) {
		List<String> pathList = new ArrayList<>();
		String basePack = packName.replace(".", "\\");
		String[] subPack = basePack.split("\\*\\*");
		String classPath = getClassPath();
		int classPathLength = classPath.length();

		if(basePack.startsWith("**")) {
			basePack = basePack.substring(3);
		}else if (basePack.contains("**")) {
			basePack = basePack.substring(0, basePack.indexOf("**"));
		}

		Path path = Paths.get(classPath + basePack);
		getPath(path, pathList);
		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		List<String> removePathList = new ArrayList<>();
		for (String content : pathList) {
			boolean flag = true;
			//用于判断当前文件系统中的路径是否匹配配置文件中的路径
			for (int i = 0; i < subPack.length; i++) {
				if (!content.contains(subPack[i])
						|| (i > 0 && content.indexOf(subPack[i]) < content.indexOf(subPack[i-1]))
						|| (!content.endsWith(subPack[subPack.length - 1]) && !subPack[subPack.length - 1].endsWith("\\"))) {
					flag = false;
					removePathList.add(content);
					//pathList.remove(content);//遍历集合的时候不应该对集合进行删除
					break;
				}
			}
			if (flag) {
				// 获取此包的目录 建立一个File
				File dir = new File(content);
				// 如果存在 就获取包下的所有文件
				File[] dirfiles = dir.listFiles(new FileFilter() {
					// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
					public boolean accept(File file) {
						return (file.getName().endsWith(".class"));
					}
				});
				// 循环所有文件
				for (File file : dirfiles) {
					// 如果是java类文件 去掉后面的.class 只留下类名
					String className = file.getName().substring(0, file.getName().length() - 6);
					try{
						// 添加到集合中去
						// classes.add(Class.forName(packageName + '.' + className));
						// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
						Class<?> clas = Thread.currentThread().getContextClassLoader().loadClass(content.substring(classPathLength).replace("\\", ".") + '.' + className);
						Table table = clas.getAnnotation(Table.class);
						// 没有 @Table 打注解不需要创建变
						if (null == table) {
							continue;
						}
						classes.add(clas);
					}catch (ClassNotFoundException e) {
						// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
						e.printStackTrace();
					}
				}
			}
		}
		pathList.removeAll(removePathList);
		ConfigLoder.setModelPack(pathList);//把model的路径放到配置类中，用于加载用来初始化数据的类；放到ConfigLoder的目的只是为了方便传参而已
		return classes;
	}

	/**
	 * 过去需要初始化数据的 class ，也就是配置文件中和 model 相同路径的并且有用@InitData 注解的方法的类
	 * @param pathList
	 * @return
	 */
	public static Set<Class<?>> getInitDataClasses(List<String> pathList) {
		String classPath = getClassPath();
		int classPathLength = classPath.length();
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		for (String content : pathList) {
			// 获取此包的目录 建立一个File
			File dir = new File(content);
			// 如果存在 就获取包下的所有文件 包括目录
			File[] dirfiles = dir.listFiles(new FileFilter() {
				// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
				public boolean accept(File file) {
					return (file.getName().endsWith(".class"));
				}
			});
			// 循环所有文件
			for (File file : dirfiles) {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try{
					// 添加到集合中去
					Class<?> clas = Thread.currentThread().getContextClassLoader().loadClass(content.substring(classPathLength).replace("\\", ".") + '.' + className);
					Method[] methods = clas.getDeclaredMethods();
					for (Method method : methods) {
						if (method.isAnnotationPresent(InitData.class)) {
							classes.add(clas);
							break;
						}
					}
				}catch (ClassNotFoundException e) {
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
		return classes;
	}

	/**
	 * 用于递归出所有在配置文件中配置的 model 的路径
	 * @param path
	 * @param pathList
	 */
	public static void getPath(Path path, List<String> pathList) {
		if (!path.toFile().isDirectory()) {
			return;
		}
		pathList.add(path.toAbsolutePath().toString());
		DirectoryStream<Path> directoryStream = null;
		try {
			directoryStream = Files.newDirectoryStream(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for(Path item : directoryStream) {
			getPath(item, pathList);
        }
	}

	/**
	 * 获取 ClassPath 路径
	 * @return
	 */
	public static String getClassPath() {
		URL rootPath = Thread.currentThread().getContextClassLoader().getResource("/");
		String classPath = null;
		try {
			if (rootPath != null) {
				classPath = rootPath.toURI().getPath();
			}else {
//				classPath  = SysMenuServiceImpl.class.getResource("/").toURI().getPath();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (classPath.indexOf("/") == 0) {
			classPath = classPath.substring(1);
		}
		return classPath;
	}

	/**
	 * 取出list对象中的某个属性的值作为list返回
	 * @param objList
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, E> List<E> getPropertyValueList(List<T> objList, String fieldName){
		List<E> list = new ArrayList<E>();
		try{
			for (T object : objList){
				Field field = object.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				list.add((E) field.get(object));
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return list;
	}
}
