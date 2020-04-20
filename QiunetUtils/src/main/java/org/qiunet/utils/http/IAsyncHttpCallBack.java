package org.qiunet.utils.http;

/***
 *
 *
 * @author qiunet
 * 2020-04-20 17:44
 ***/
public interface IAsyncHttpCallBack {

	void onFail(Exception e);

	void onResponse(String result);
}
