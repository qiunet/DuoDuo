package org.qiunet.test.handler.proto;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.function.badword.DefaultBadWord;
import org.qiunet.function.badword.LoadBadWordEventData;
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
		ClassScanner.getInstance(ScannerType.CHANNEL_DATA ,ScannerType.EVENT).scanner();
		LoadBadWordEventData.valueOf(new DefaultBadWord(new String[] {"毛泽东"})).fireEventHandler();
	}
	@Test
	public void testByteArray(){
		WsPbLoginRequest request = WsPbLoginRequest.valueOf("qiunet", "qiuyang", 11);

		WsPbLoginRequest loginRequest1 = ProtobufDataManager.decode(WsPbLoginRequest.class, request.toByteArray());
		Assertions.assertEquals("qiunet", loginRequest1.getAccount());
	}

	@Test
	public void testByteBuf (){
		WsPbLoginRequest request = WsPbLoginRequest.valueOf("qiunet", "qiuyang", 11);

		for (int i = 0; i < 5; i++) {
			ByteBuf byteBuf = request.toByteBuf();
			try {
				WsPbLoginRequest loginRequest2 = ProtobufDataManager.decode(WsPbLoginRequest.class, byteBuf.nioBuffer());
				Assertions.assertEquals("qiunet", loginRequest2.getAccount());
			}finally {
				byteBuf.release();
			}
		}
	}

	@Test
	public void testParamCheck() {
		WsPbLoginRequest request1 = WsPbLoginRequest.valueOf("qiunet", "qiuyang", 1);
		StatusResultException exception = Assertions.assertThrows(StatusResultException.class, () -> {
			ChannelDataMapping.requestCheck(null, request1);
		});
		Assertions.assertEquals(IGameStatus.NUMBER_PARAM_ERROR, exception.getStatus());

		WsPbLoginRequest request2 = WsPbLoginRequest.valueOf("qiunet", "毛泽~东", 11);
		exception = Assertions.assertThrows(StatusResultException.class, () -> {
			ChannelDataMapping.requestCheck(null, request2);
		});
		Assertions.assertEquals(IGameStatus.STRING_PARAM_BAD_WORD_ERROR, exception.getStatus());


	}
}
