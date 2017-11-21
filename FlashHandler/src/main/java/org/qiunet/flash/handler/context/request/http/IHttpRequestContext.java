package org.qiunet.flash.handler.context.request.http;

import org.qiunet.flash.handler.context.request.IRequestContext;


/**
 * Created by qiunet.
 * 17/11/21
 */
public interface IHttpRequestContext<RequestData> extends IRequestContext<RequestData>, IHttpRequest<RequestData> {

}
