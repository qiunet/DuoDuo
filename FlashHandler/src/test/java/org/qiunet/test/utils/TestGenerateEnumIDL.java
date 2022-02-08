package org.qiunet.test.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.util.proto.ProtoIDLGenerator;
import org.qiunet.flash.handler.util.proto.ProtobufVersion;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/***
 *
 * @author qiunet
 * 2022/1/12 14:17
 */
public class TestGenerateEnumIDL {

	@Test
	public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method method = ProtoIDLGenerator.class.getDeclaredMethod("generateEnumIDL", StringBuilder.class, Class.class);
		ReflectUtil.makeAccessible(method);
		StringBuilder sb = new StringBuilder();
		method.invoke(new ProtoIDLGenerator(ProtobufVersion.V3), sb, SortType.class);

		Assertions.assertEquals(sb.toString(), "enum SortType {  \n" +
				"\tSortType_A=0;\n" +
				"\tSortType_B=1;\n" +
				"\tSortType_C=2;\n" +
				"}\n\n\n");
	}

	public enum SortType {
		A,
		B,
		C;
		public static final SortType D = C;
		private static final List<SortType> values = Arrays.asList(values());
	}
}
