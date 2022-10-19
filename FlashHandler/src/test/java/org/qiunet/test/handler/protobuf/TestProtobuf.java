package org.qiunet.test.handler.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-10-16 11:53
 */
public class TestProtobuf {

	@Test
	public void test() throws IOException {
		PbData pbData = new PbData();
		pbData.setName("qiunet");
		pbData.setPlayerId(111);

		Codec<PbData> codec = ProtobufProxy.create(PbData.class);
		byte[] encode = codec.encode(pbData);

		PbData pbData1 = codec.decode(encode);
		Assertions.assertEquals(111, pbData1.getPlayerId());
	}

	@Test
	public void testDecode() {
		List<Long> longs = Lists.newArrayList(1L, 2L, 3L);
		String name = "qiunet";
		int level = 10;

		RequestData data = RequestData.valueOf(name, level, longs);
		byte[] bytes = data.toByteArray();


		for (int i = 0; i < 100000; i++) {
			RequestData decode = ProtobufDataManager.decode(RequestData.class, ByteBuffer.wrap(bytes));

			Assertions.assertEquals(decode.getName(), name);
			Assertions.assertEquals(decode.getLevel(), level);
			Assertions.assertEquals(decode.getList(), longs);
		}

	}
}
