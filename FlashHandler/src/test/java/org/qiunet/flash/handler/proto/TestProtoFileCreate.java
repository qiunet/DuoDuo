package org.qiunet.flash.handler.proto;

import org.qiunet.flash.handler.common.enums.ProtoGeneratorModel;
import org.qiunet.flash.handler.util.GeneratorProtoFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/***
 *
 *
 * @author qiunet
 * 2020-09-25 12:54
 */
public class TestProtoFileCreate {

	public static void main(String[] args) throws Exception {
		Path path = Paths.get("C:\\Users\\qiune\\workSpace");
		File file = path.toFile();

		GeneratorProtoFile.generator(file, ProtoGeneratorModel.ALL_IN_ONE, "org.qiunet");
	}
}
