package org.qiunet.test.handler.proto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;


/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:57
 */
public class TestProtobufData {
	@BeforeAll
	public static void beforeExec() throws Exception {
		ClassScanner.getInstance(ScannerType.CHANNEL_DATA).scanner();
	}
	@Test
	public void test(){
		WsPbLoginRequest request = WsPbLoginRequest.valueOf("qiunet", "qiuyang", 11);
		byte[] bytes = request.toByteArray();

		WsPbLoginRequest loginRequest = ProtobufDataManager.decode(WsPbLoginRequest.class, bytes);
		Assertions.assertEquals("qiunet", loginRequest.getAccount());
	}
}
