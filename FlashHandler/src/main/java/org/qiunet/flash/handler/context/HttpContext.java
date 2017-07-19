package org.qiunet.flash.handler.context;

import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.response.IResponse;

/**
 * @author qiunet
 *         Created on 17/3/17 14:28.
 */
public class HttpContext<RequestData, ResponseData> extends BaseContext<RequestData> implements IResponse<ResponseData> {

	private ResponseData response;

	public HttpContext(IHandler handler, RequestData requestData, ResponseData response)  {
		super(handler, requestData);
		this.response = response;
	}

	@Override
	public void response(ResponseData responseData) {
//		OutputByteStream out = OutputByteStreamBuilder.getOutputByteStream();
//		IHandler handler = RequestHandlerMapping.getInstance().getHandler(requestData.getLeader().getCmdId());
//		try {
//			responseData.dataWriter(out);
//			byte [] datas = out.getBytes();
//			if (((IHttpHandler)handler).needRecodeData()) {
//				// 处理重复的数据
//				// uid + 上行时间(序列码) + cmdId 为key
//			}
//			response.setStatus(HttpStatus.SC_OK);
//			response.getOutputStream().write(datas);
//			response.setContentLength(datas.length);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				out.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}
}
