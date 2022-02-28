package org.qiunet.test.handler.proto;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.util.proto.GeneratorProtoFeature;
import org.qiunet.flash.handler.util.proto.ProtoIDLGenerator;
import org.qiunet.flash.handler.util.proto.ProtobufVersion;

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

	@Test
	public void testCreateProto(){
		GeneratorProtoFeature.features = Sets.newHashSet(GeneratorProtoFeature.ENUM_TO_INT);

		String idl = new ProtoIDLGenerator(ProtobufVersion.V2).getIDL(RecursiveClass2.class);

		Assertions.assertEquals(idl, "" +
				"message RecursiveClass2 {  \n" +
				"\toptional int32 intVal=1;\n" +
				"\t// 性别\n" +
				"\t// 1=男\t2=女\t\n" +
				"\toptional int32 genderType=2;\n" +
				"\t// list\n" +
				"\t// 1=男\t2=女\t\n" +
				"\trepeated int32 typeList=3 [packed = true];\n" +
				"\t// obj\n" +
				"\toptional RecursiveObj2 obj2=4;\n" +
				"}\n\n\n");
	}
}
