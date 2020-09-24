package org.qiunet.flash.handler.util;

import org.qiunet.flash.handler.common.enums.ProtoGeneratorModel;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.io.File;

/***
 * 生成proto 文件给客户端.
 *
 * @author qiunet
 * 2020-09-23 16:34
 */
public enum GeneratorProtoFile implements IApplicationContextAware {
	instance,;
	private IApplicationContext context;
	/**
	 *
	 * @param directory 生成proto存放的文件夹
	 */
	public static void generator(File directory, ProtoGeneratorModel model, String packetPrefix) throws Exception {
		ClassScanner.getInstance(ScannerType.TESTER).scanner(packetPrefix);
	}

	@Override
	public void setApplicationContext(IApplicationContext context) throws Exception {
		this.context = context;
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.TESTER;
	}
}
