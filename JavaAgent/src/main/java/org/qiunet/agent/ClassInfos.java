package org.qiunet.agent;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/***
 * 所有的class 存放在指定的classPath
 *
 * 加载完后. 加载器会删除
 */
public class ClassInfos {
	/***
	 * class在哪个目录下.
	 */
	private String classPath;
	/***
	 * className
	 * 例如: org.qiunet.agent.ClassInfos
	 *
	 * 使用时候, 根据classPath 和 最后的ClassInfos + ".class"
	 * 拼出文件名称
	 */
	private Set<String> classNames = new HashSet<>();

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public void addClass(String className) {
		this.classNames.add(className);
	}

	public String getClassPath() {
		return classPath;
	}

	public Set<String> getClassNames() {
		return classNames;
	}

	public static ClassInfos parse(String str) {
		ClassInfos classInfos = new ClassInfos();
		String [] strs = str.split(",");
		classInfos.classPath = strs[0];
		if (strs.length > 1) {
			for (int i = 1; i < strs.length; i++) {
				classInfos.addClass(strs[i]);
			}
		}
		return classInfos;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",");
		joiner.add(classPath);
		classNames.forEach(str -> joiner.add(str));
		return joiner.toString();
	}
}
