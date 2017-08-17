package org.qiunet.flash.handler.context;

import org.apache.http.HttpStatus;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.IHttpHandler;
import org.qiunet.flash.handler.handler.RequestHandlerMapping;
import org.qiunet.flash.handler.handler.response.IResponse;
import org.qiunet.flash.handler.iodata.adapter.OutputByteStreamBuilder;
import org.qiunet.flash.handler.iodata.base.OutputByteStream;

import javax.servlet.http.HttpServletResponse;

/**
 * @author qiunet
 *         Created on 17/3/17 14:28.
 */
public class HttpContext<RequestData, ResponseData> extends BaseContext<RequestData> implements IResponse<ResponseData> {
	private HttpServletResponse response;

	public HttpContext(IHandler handler, RequestData requestData, HttpServletResponse response)  {
		super(handler, requestData);
		this.response = response;
	}

	@Override
	public void response(int protocolId, ResponseData responseData) {
		OutputByteStream out = OutputByteStreamBuilder.getOutputByteStream();
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(getHandler().getProtocolID());
		try {
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

	@Override
	public boolean handler() {
		return false;
	}
}
