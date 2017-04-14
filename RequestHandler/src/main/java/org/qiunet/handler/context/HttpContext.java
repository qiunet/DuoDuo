package org.qiunet.handler.context;

import org.apache.http.HttpStatus;
import org.qiunet.handler.handler.IHandler;
import org.qiunet.handler.handler.IHttpHandler;
import org.qiunet.handler.handler.RequestHandlerMapping;
import org.qiunet.handler.iodata.adapter.OutputByteStreamBuilder;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.net.AbstractRequestData;
import org.qiunet.handler.iodata.net.AbstractResponseData;

import javax.servlet.http.HttpServletResponse;
/**
 * @author qiunet
 *         Created on 17/3/17 14:28.
 */
public class HttpContext extends BaseHttpContext {
	
	private HttpServletResponse response;
	
	public HttpContext (AbstractRequestData requestData, HttpServletResponse response)  {
		super(requestData);
		this.response = response;
	}

	@Override
	public void response(AbstractResponseData responseData) {
		OutputByteStream out = OutputByteStreamBuilder.getOutputByteStream();
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(requestData.getLeader().getCmdId());
		try {
			responseData.dataWriter(out);
			byte [] datas = out.getBytes();
			if (((IHttpHandler)handler).needRecodeData()) {
				// 处理重复的数据
				// uid + 上行时间(序列码) + cmdId 为key
			}
			response.setStatus(HttpStatus.SC_OK);
			response.getOutputStream().write(datas);
			response.setContentLength(datas.length);
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
