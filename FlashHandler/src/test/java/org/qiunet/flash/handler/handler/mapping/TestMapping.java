package org.qiunet.flash.handler.handler.mapping;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.flash.handler.handler.http.LoginHandler;
import org.qiunet.flash.handler.handler.http.LoginProtobufHandler;
import org.qiunet.flash.handler.handler.http.UriHandler;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class TestMapping extends RequestHandlerScanner {
	@Test
	public void scannerRequestHandler() throws URISyntaxException, MalformedURLException, ClassNotFoundException {
		IHandler handler = RequestHandlerMapping.getInstance().getGameHandler(1000);
		Assert.assertEquals(handler.getDataType() , DataType.STRING);
		Assert.assertTrue(handler instanceof LoginHandler);

		handler = RequestHandlerMapping.getInstance().getGameHandler(1001);
		Assert.assertEquals(handler.getDataType() , DataType.PROTOBUF);
		Assert.assertTrue(handler instanceof LoginProtobufHandler);

		IHttpHandler httpHandler = RequestHandlerMapping.getInstance().getUriPathRequestHandler("/back");
		Assert.assertNotNull(httpHandler);
		Assert.assertTrue(httpHandler instanceof UriHandler);
	}
}
