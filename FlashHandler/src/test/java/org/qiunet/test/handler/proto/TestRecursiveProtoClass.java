package org.qiunet.test.handler.proto;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.util.proto.ProtoIDLGenerator;

import java.util.Set;

/***
 *
 * @author qiunet
 * 2022/2/9 11:06
 */
public class TestRecursiveProtoClass {

	@Test
	public void testEnum() {
		Set<Class<?>> classSet = Sets.newHashSet();
		ProtoIDLGenerator.recursiveObjClass(TcpPbLoginRequest.class, classSet);

		Assertions.assertEquals(1, classSet.size());

		Assertions.assertTrue(classSet.contains(GenderType.class));
	}

	@Test
	public void testObjAndList() {
		Set<Class<?>> classSet = Sets.newHashSet();
		ProtoIDLGenerator.recursiveObjClass(RecursiveClass1.class, classSet);

		Assertions.assertEquals(2, classSet.size());

		Assertions.assertTrue(classSet.contains(RecursiveObj2.class));
		Assertions.assertTrue(classSet.contains(RecursiveObj1.class));
	}

	@Test
	public void testMap() {
		Set<Class<?>> classSet = Sets.newHashSet();
		ProtoIDLGenerator.recursiveObjClass(RecursiveObj3.class, classSet);

		Assertions.assertEquals(1, classSet.size());

		Assertions.assertTrue(classSet.contains(RecursiveObj2.class));
	}
}
