package org.qiunet.utils.http;

import com.alibaba.fastjson2.JSONObject;

/***
 * 自定义返回结果.
 * @author qiunet
 * 2020-04-22 19:39
 ***/
public interface IResultSupplier<T> {
	/**
	 * 将body 返回string
	 */
	IResultSupplier<String> STRING_SUPPLIER = HttpResponse::body;
	/**
	 * 将body 返回 byte[]
	 */
	IResultSupplier<byte []> BYTE_ARR_SUPPLIER = HttpResponse::getBytes;
	/**
	 * 将body 返回 jsonObject
	 */
	IResultSupplier<JSONObject> JSON_OBJECT_SUPPLIER = response -> JSONObject.parseObject(response.body());
	/***
	 * 如果想获得返回的是字符串， 可以通过 response.body().string()
	 * 如果获得返回的二进制字节数组，则调用 response.body().bytes()
	 * 如果想拿到返回的inputStream，则调 response.body().byteStream()
	 * @param response
	 * @return
	 * @throws Exception
	 */
	T result(HttpResponse response) throws Exception;

	/**
	 * 返回response 自身
	 * @return
	 */
	static IResultSupplier<HttpResponse> identity() {
		return response -> response;
	}
}
