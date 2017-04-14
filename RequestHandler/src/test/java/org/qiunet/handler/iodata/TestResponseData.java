package org.qiunet.handler.iodata;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.handler.iodata.adapter.InputByteStreamBuilder;
import org.qiunet.handler.iodata.adapter.OutputByteStreamBuilder;
import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.entitys.LoginResponseData;

/**
 * @author qiunet
 *         Created on 17/3/10 21:57.
 */
public class TestResponseData {
	@Test
	public void testResponseData() {
		LoginResponseData responseData = new LoginResponseData();
		responseData.setUid(1000);
		responseData.setSid("qiuyang");
		
		boolean exception = false;
		OutputByteStream out = OutputByteStreamBuilder.getOutputByteStream();
		try {
			responseData.dataWriter(out);
			byte [] bytes = out.getBytes();
			InputByteStream in = InputByteStreamBuilder.getInputByteStream(bytes);
			responseData = new LoginResponseData();
			responseData.dataReader(in);
			
			Assert.assertTrue(responseData.getUid() == 1000);
			Assert.assertTrue(responseData.getSid().equals("qiuyang"));
			Assert.assertTrue(10 == responseData.getLeader().getCmdId());
		} catch (Exception e) {
			exception = true;
			e.printStackTrace();
		}
		Assert.assertFalse(exception);
	}
}
