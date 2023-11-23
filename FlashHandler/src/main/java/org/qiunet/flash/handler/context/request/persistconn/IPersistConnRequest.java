package org.qiunet.flash.handler.context.request.persistconn;

import org.qiunet.flash.handler.context.request.IRequest;
import org.qiunet.flash.handler.context.sender.ISessionHolder;


/**
 * 长连接请求接口
 * Created by qiunet.
 * 17/12/2
 */
public interface IPersistConnRequest<RequestData> extends IRequest<RequestData>, ISessionHolder {

}
