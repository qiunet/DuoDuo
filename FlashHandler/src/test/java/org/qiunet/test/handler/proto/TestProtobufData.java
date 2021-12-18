package org.qiunet.test.handler.proto;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
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
	@BeforeClass
	public static void beforeExec() throws Exception {
		ClassScanner.getInstance(ScannerType.PROTOBUF_DATA, ScannerType.CHANNEL_DATA).scanner();
	}
	@Test
	public void test(){
		WsPbLoginRequest request = WsPbLoginRequest.valueOf("qiunet", "qiuyang", 11);
		byte[] bytes = request.toByteArray();

		WsPbLoginRequest loginRequest = ProtobufDataManager.decode(WsPbLoginRequest.class, bytes);
		Assert.assertEquals("qiunet", loginRequest.getAccount());
	}
}
