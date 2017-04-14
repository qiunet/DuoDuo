package org.qiunet.handler.jetty.server.Haneler;

import org.apache.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.qiunet.handler.context.HttpContext;
import org.qiunet.handler.handler.IHandler;
import org.qiunet.handler.handler.IHttpHandler;
import org.qiunet.handler.handler.RequestHandlerMapping;
import org.qiunet.handler.handler.acceptor.Acceptor;
import org.qiunet.handler.handler.intecepter.Intercepter;
import org.qiunet.handler.intercepter.HandlerIntercepter;
import org.qiunet.handler.iodata.adapter.InputByteStreamBuilder;
import org.qiunet.handler.iodata.adapter.OutputByteStreamBuilder;
import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.net.AbstractRequestData;
import org.qiunet.handler.iodata.net.AbstractResponseData;
import org.qiunet.handler.iodata.net.LeaderIoData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jetty 接收请求请求 和  响应的处理
 * @author qiunet
 *         Created on 17/3/17 12:20.
 */
public class JettyHandler extends AbstractHandler {
	private volatile static JettyHandler instance;
	private Acceptor acceptor;

	private JettyHandler() {
		instance = this;
		Intercepter intercepter = new HandlerIntercepter();
		acceptor = Acceptor.create(intercepter);
	}

	public static JettyHandler getInstance() {
		if (instance == null) {
			synchronized (JettyHandler.class) {
				if (instance == null)
				{
					new JettyHandler();
				}
			}
		}
		return instance;
	}
	
	@Override
	public void handle(String s, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int length = request.getContentLength();
		byte [] bytes = new byte [length];
		request.getInputStream().read(bytes);
		InputByteStream tempIn = InputByteStreamBuilder.getInputByteStream(bytes);
		InputByteStream in = null;
		LeaderIoData leaderIoData = new LeaderIoData();
		try {
			leaderIoData.dataReader(tempIn);
			tempIn.close();
			IHandler handler = RequestHandlerMapping.getInstance().getHandler(leaderIoData.getCmdId());

			in = InputByteStreamBuilder.getInputByteStream(bytes);
			AbstractRequestData requestData = handler.getRequestDataObj();
			requestData.dataReader(in);
			acceptor.handler(new HttpContext(requestData, response));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (tempIn != null ) tempIn.close();
				if (in != null) in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 反馈一个错误的代码给客户端
	 * @param response 
	 * @param errorResponseData 
	 */
	public void responseError(HttpServletResponse response, AbstractResponseData errorResponseData) {
		OutputByteStream out = OutputByteStreamBuilder.getOutputByteStream();
		try {
			errorResponseData.dataWriter(out);
			byte [] datas = out.getBytes();
			response.setStatus(HttpStatus.SC_OK);
			response.setContentLength(datas.length);
			response.getOutputStream().write(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
