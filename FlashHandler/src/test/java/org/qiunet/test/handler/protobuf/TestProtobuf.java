package org.qiunet.test.handler.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
}
