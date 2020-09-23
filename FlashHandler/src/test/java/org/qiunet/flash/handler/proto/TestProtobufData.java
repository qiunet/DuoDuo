package org.qiunet.flash.handler.proto;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.utils.protobuf.ProtobufDataManager;
import org.qiunet.utils.scanner.ClassScanner;


/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:57
 */
public class TestProtobufData {
	@BeforeClass
	public static void beforeExec() throws Exception {
		ClassScanner.getInstance().scanner();
	}
	@Test
	public void test(){
		LoginRequest request = LoginRequest.valueOf("qiunet", "qiuyang", 11);
		byte[] bytes = request.toByteArray();

		LoginRequest loginRequest = ProtobufDataManager.decode(LoginRequest.class, bytes);
		Assert.assertEquals("qiunet", loginRequest.getAccount());
	}
}
