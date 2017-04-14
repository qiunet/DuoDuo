package org.qiunet.handler.handler.intecepter;

import org.qiunet.handler.context.IContext;
/**
 *  拦截器.
 *  可以多组一起使用.
 * @author qiunet
 *         Created on 17/3/6 18:04.
 */
public interface Intercepter {
	/**
	 * 前置处理.
	 * @param context
	 * @return
	 */
	public void preHandler(IContext context);
	/**
	 * 
	 * @param context
	 */
	public void handler(IContext context);
	/**
	 * 后置处理
	 * @param context
	 */
	public void postHandler(IContext context);
	/**
	 * 异常处理
	 * @param e
	 */
	public void throwCause(IContext context, Exception e);
}
