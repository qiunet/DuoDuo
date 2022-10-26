package org.qiunet.test.handler.proto;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.util.proto.*;

import java.io.File;

/***
 *
 * @author qiunet
 * 2022/2/9 11:06
 */
public class TestRecursiveProtoClass {

	@Test
	public void testEnum() {
		GeneratorProtoCache cache = new GeneratorProtoCache(ProtobufVersion.V3);
		ProtoIDLGenerator.recursiveObjClass(TcpPbLoginRequest.class, cache);

		Assertions.assertEquals(1, cache.getCommonProtoTypes().size());

		Assertions.assertTrue(cache.getCommonProtoTypes().contains(GenderType.class));
	}

	@Test
	public void testObjAndList() {
		GeneratorProtoCache cache = new GeneratorProtoCache(ProtobufVersion.V3);
		ProtoIDLGenerator.recursiveObjClass(RecursiveClass1.class, cache);

		Assertions.assertEquals(2, cache.getCommonProtoTypes().size());

		Assertions.assertTrue(cache.getCommonProtoTypes().contains(RecursiveObj2.class));
		Assertions.assertTrue(cache.getCommonProtoTypes().contains(RecursiveObj1.class));
	}

	@Test
	public void testMap() {
		GeneratorProtoCache cache = new GeneratorProtoCache(ProtobufVersion.V3);
		ProtoIDLGenerator.recursiveObjClass(RecursiveObj3.class, cache);

		Assertions.assertEquals(0, cache.getCommonProtoTypes().size());
		Assertions.assertFalse(cache.getCommonProtoTypes().contains(RecursiveObj2.class));
	}

	@Test
	public void testCreateProto(){
		GeneratorProtoFeature.features.put(GeneratorProtoFeature.ENUM_TO_INT, null);

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


	@Test
	public void testGeneratorParam() {
		Assertions.assertDoesNotThrow(() -> {
			new GeneratorProtoParam(ProtoGeneratorModel.GROUP_BY_MODULE,
					Lists.newArrayList(RecursiveTest.class, Recursive1.class, Recursive2.class),
					ProtobufVersion.V3, new File(""));
		});
	}
}
