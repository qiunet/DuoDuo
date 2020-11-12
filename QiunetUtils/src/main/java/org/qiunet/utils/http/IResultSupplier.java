package org.qiunet.utils.http;

import okhttp3.Response;

/***
 * 自定义返回结果.
 * @author qiunet
 * 2020-04-22 19:39
 ***/
public interface IResultSupplier<T> {
	/***
	 * 如果想获得返回的是字符串， 可以通过 response.body().string()
	 * 如果获得返回的二进制字节数组，则调用 response.body().bytes()
	 * 如果想拿到返回的inputStream，则调 response.body().byteStream()
	 * @param response
	 * @return
	 * @throws Exception
	 */
	T result(Response response) throws Exception;

	/**
	 * 返回response 自身
	 * @return
	 */
	static IResultSupplier<Response> identity() {
		return response -> response;
	}
}
