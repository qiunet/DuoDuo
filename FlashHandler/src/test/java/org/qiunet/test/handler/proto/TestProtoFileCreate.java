package org.qiunet.test.handler.proto;

import org.qiunet.flash.handler.common.enums.ProtoGeneratorModel;
import org.qiunet.flash.handler.util.GeneratorProtoFile;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

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
		Path path = Paths.get("C:\\Users\\qiunet\\Desktop");
		File file = path.toFile();
		ClassScanner.getInstance(ScannerType.SERVER).scanner("org.qiunet");

		GeneratorProtoFile.generator(file, ProtoGeneratorModel.ALL_IN_ONE);
	}
}
