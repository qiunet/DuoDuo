package org.qiunet.test.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.util.proto.ProtoCompatible;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

/***
 *
 * @author qiunet
 * 2022/11/11 15:41
 */
public class TestProtoCompatible {
	private static ProtoCompatible compatible;

	private static String testProtoPath;
	private static String delMessagePath;
	private static String changeFieldPath;
	private static String addFieldPath;

	private static String delFieldPath;
	private static String originPath;

	@BeforeAll
	public static void init() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("proto");
		delMessagePath = Paths.get(url.getPath(), "basic", "delMessage.proto").toString();
		changeFieldPath = Paths.get(url.getPath(), "basic", "changeField.proto").toString();
		addFieldPath = Paths.get(url.getPath(), "basic", "addField.proto").toString();
		delFieldPath = Paths.get(url.getPath(), "basic", "delField.proto").toString();
		originPath = Paths.get(url.getPath(), "basic", "origin.proto").toString();
		testProtoPath = Paths.get(url.getPath(), "test.proto").toString();
		compatible = new ProtoCompatible(new File(url.getFile()));
	}

	@Test
	public void testAddField() {
		FileUtil.copy(addFieldPath, testProtoPath);
		ProtoCompatible newCompatible = new ProtoCompatible(compatible.getDir());
		Assertions.assertTrue(compatible.compatible(newCompatible));
	}


	@Test
	public void testDelField() {
		FileUtil.copy(delFieldPath, testProtoPath);
		ProtoCompatible newCompatible = new ProtoCompatible(compatible.getDir());
		Assertions.assertFalse(compatible.compatible(newCompatible));
	}

	@Test
	public void testChangeField() {
		FileUtil.copy(changeFieldPath, testProtoPath);
		ProtoCompatible newCompatible = new ProtoCompatible(compatible.getDir());
		Assertions.assertFalse(compatible.compatible(newCompatible));
	}

	@Test
	public void testDelMessage() {
		FileUtil.copy(delMessagePath, testProtoPath);
		ProtoCompatible newCompatible = new ProtoCompatible(compatible.getDir());
		Assertions.assertFalse(compatible.compatible(newCompatible));
	}


	@AfterEach
	public void after (){
		FileUtil.copy(originPath, testProtoPath);
	}
}
