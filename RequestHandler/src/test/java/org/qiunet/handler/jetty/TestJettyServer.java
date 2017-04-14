package org.qiunet.handler.jetty;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.handler.iodata.adapter.InputByteStreamBuilder;
import org.qiunet.handler.iodata.adapter.OutputByteStreamBuilder;
import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.entitys.LoginRequestData;
import org.qiunet.handler.iodata.entitys.LoginResponseData;
import org.qiunet.handler.jetty.server.JettyServer;

import java.util.concurrent.CountDownLatch;


/**
 * Created by qiunet on 3/20/17.
 */
public class TestJettyServer {
	private final Logger logger = Logger.getLogger(getClass());

	private final JettyServer server = new JettyServer();
	private CountDownLatch latch = new CountDownLatch(1);
	@Test
	public void testJettyServer() throws Exception {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				server.start();
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();

		HttpClient client = new HttpClient();
		client.start();

		Request request = client.newRequest("http://localhost:8080/");
		request.method(HttpMethod.POST);

		LoginRequestData requestData = new LoginRequestData();
		requestData.getLeader().setCmdId((short) 101);
		requestData.setOpenid("qiunet");
		requestData.setSecret("qiuyang");
		requestData.setToken("net");
		OutputByteStream out = OutputByteStreamBuilder.getOutputByteStream();
		requestData.dataWriter(out);

		request.content(new BytesContentProvider(out.getBytes()));
		ContentResponse response = request.send();
		Assert.assertTrue(response.getStatus() == HttpStatus.OK_200) ;
		InputByteStream ibs = InputByteStreamBuilder.getInputByteStream(response.getContent());
		LoginResponseData responseData = new LoginResponseData();
		responseData.dataReader(ibs);
		ibs.close();
		out.close();

		Assert.assertEquals("qiuyang", responseData.getSid());
		Assert.assertTrue(1000 == responseData.getUid());
	}
}
