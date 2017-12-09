package org.qiunet.test.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestProtobuf {
	@Test
	public void testHeader() throws InvalidProtocolBufferException {
		PlayerIndexProto.PlayerIndexResponse playerIndexResponse = PlayerIndexProto.PlayerIndexResponse.newBuilder()
				.setHeader(HeaderProto.ResponseHeader.newBuilder()
						.setRet(10001)
						.setMsg("12345")
						)
				.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123456).setCount(22).build())
				.build();

		byte [] bytes = playerIndexResponse.toByteArray();

		LoginProto.LoginResponse loginResponse = LoginProto.LoginResponse.parseFrom(bytes);
		Assert.assertEquals(loginResponse.getHeader().getRet() , playerIndexResponse.getHeader().getRet());

		HeaderProto.ResponseCheck check = HeaderProto.ResponseCheck.parseFrom(bytes);
		Assert.assertEquals(check.getHeader().getRet(), playerIndexResponse.getHeader().getRet());
	}
}
