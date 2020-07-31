package org.qiunet.tests.proto;

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
						.setMsg("测试")
						)
				.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123456).setCount(22).build())
				.build();

		byte [] bytes = playerIndexResponse.toByteArray();

		LoginProto.LoginResponse loginResponse = LoginProto.LoginResponse.parseFrom(bytes);
		Assert.assertEquals(loginResponse.getHeader().getRet() , playerIndexResponse.getHeader().getRet());
		Assert.assertEquals(loginResponse.getHeader().getMsg() , playerIndexResponse.getHeader().getMsg());

		HeaderProto.ErrorResponse error = HeaderProto.ErrorResponse.parseFrom(bytes);
		Assert.assertEquals(error.getHeader().getRet(), playerIndexResponse.getHeader().getRet());
		Assert.assertEquals(error.getHeader().getMsg(), playerIndexResponse.getHeader().getMsg());
	}

	@Test
	public void testOnlyHeaderToProtobuf() throws InvalidProtocolBufferException {
		HeaderProto.ResponseHeader header = HeaderProto.ResponseHeader.newBuilder()
				.setRet(10001)
				.setMsg("测试")
				.build();

		byte [] bytes = header.toByteArray();
		LoginProto.LoginResponse response = LoginProto.LoginResponse.parseFrom(bytes);

		Assert.assertNotEquals(response.getHeader().getRet() , header.getRet());
		Assert.assertNotEquals(response.getHeader().getMsg() , header.getMsg());
	}
}
