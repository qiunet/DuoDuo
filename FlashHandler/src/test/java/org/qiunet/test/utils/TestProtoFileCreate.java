package org.qiunet.test.utils;

import org.qiunet.flash.handler.util.proto.GeneratorProtoFile;
import org.qiunet.flash.handler.util.proto.ProtoGeneratorModel;
import org.qiunet.flash.handler.util.proto.ProtobufVersion;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.io.File;

/***
 *
 * @author qiunet
 * 2022/5/20 15:09
 */
public class TestProtoFileCreate {

	public static void main(String[] args) throws Exception {
		ClassScanner.getInstance(ScannerType.GENERATOR_PROTO).scanner();

		String svnDir = Thread.currentThread().getContextClassLoader().getResource(".").getFile();
		File protoDir = new File(svnDir, "proto");
		if (! protoDir.exists()) {
			protoDir.mkdirs();
		}
		GeneratorProtoFile.generator(
				protoDir, ProtoGeneratorModel.GROUP_BY_MODULE, ProtobufVersion.V2);
	}
}
