package org.qiunet.handler.iodata;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.handler.iodata.adapter.InputByteStreamBuilder;
import org.qiunet.handler.iodata.adapter.OutputByteStreamBuilder;
import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.entitys.LoginRequestData;

/**
 * @author qiunet
 *         Created on 17/3/10 18:08.
 */
public class TestRequestData {
	
	@Test
	public void testRequestData(){
		LoginRequestData loginRequestData = new LoginRequestData();
		loginRequestData.getCommon().setUid(1000);
		loginRequestData.getLeader().setCmdId((short) 10);
		
		loginRequestData.setOpenid("qiunet");
		loginRequestData.setSecret("qiuyang");
		loginRequestData.setToken("xiangyang");
		
		boolean exception = false;
		try {
			OutputByteStream out = OutputByteStreamBuilder.getOutputByteStream();
			loginRequestData.dataWriter(out);

			byte [] data = out.getBytes();
			InputByteStream in = InputByteStreamBuilder.getInputByteStream(data);
			loginRequestData = new LoginRequestData();
			loginRequestData.dataReader(in);
			
			Assert.assertTrue(loginRequestData.getLeader().getCmdId() == 10);
			Assert.assertTrue(loginRequestData.getCommon().getUid() == 1000);
			Assert.assertEquals("qiunet", loginRequestData.getOpenid());
			Assert.assertEquals("qiuyang", loginRequestData.getSecret());
			Assert.assertEquals("xiangyang", loginRequestData.getToken());
		} catch (Exception e) {
			e.printStackTrace();
			exception = true;
		}
		Assert.assertFalse(exception);
	}
}
