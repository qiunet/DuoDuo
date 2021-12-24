package org.qiunet.utils.test.secret;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.secret.ProtocolUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by qiunet.
 * 17/10/11
 */
public class TestProtocol {
	@Test
	public void testProtocol() throws UnsupportedEncodingException {
		String str = "我们都是中国人, 热爱祖国, 热爱人民. ";
		byte [] srcBytes = str.getBytes(StandardCharsets.UTF_8);
		short chunkSize = 10;
		byte [] secritBytes = ProtocolUtil.encryptData(srcBytes, chunkSize);
		byte [] originBytes = ProtocolUtil.decryptData(secritBytes, chunkSize);
		String originStr = new String(originBytes, StandardCharsets.UTF_8);
		Assertions.assertEquals(originStr, str);
	}
}
