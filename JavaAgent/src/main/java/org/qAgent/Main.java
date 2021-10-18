package org.qAgent;

import java.io.File;

/***
 *
 *
 * @author qiunet
 * 2021/10/18 10:32
 **/
public class Main {

	public static void main(String[] args) {
		File file = new File("/Users/qiunet/Desktop/JavaAgent.class");
		ClassFile classFile = new ClassFile(file);
		System.out.println(classFile.getName());
	}
}
