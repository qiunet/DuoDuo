package org.qiunet.cross.rdc;


import java.util.function.Function;

/***
 * 一个远程数据调用的处理.
 *
 * @author qiunet
 * 2020-10-22 17:38
 */
public interface IRdcHandler<Req extends IRdcRequest, Resp extends IRdcResponse> {

	/**
	 * 处理远程数据调用
	 * 自己在线程安全的地方调用 {@link DRdc#handler(Function)}
	 * rdc有2秒超时时间. 2秒没有处理. 就会超时.
	 *
	 * @param rdc 远程数据调用处理的对象.
	 */
	void handler(DRdc<Req, Resp> rdc);
}
