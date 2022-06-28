package org.qiunet.utils.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.Response;

import java.util.Objects;

/***
 * 自定义返回结果.
 * @author qiunet
 * 2020-04-22 19:39
 ***/
public interface IResultSupplier<T> {
	/**
	 * 将body 返回string
	 */
	IResultSupplier<String> STRING_SUPPLIER = response -> Objects.requireNonNull(response.body()).string();
	/**
	 * 将body 返回 byte[]
	 */
	IResultSupplier<byte []> BYTE_ARR_SUPPLIER = response -> Objects.requireNonNull(response.body()).bytes();
	/**
	 * 将body 返回 jsonObject
	 */
	IResultSupplier<JSONObject> JSON_OBJECT_SUPPLIER = response -> JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
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
