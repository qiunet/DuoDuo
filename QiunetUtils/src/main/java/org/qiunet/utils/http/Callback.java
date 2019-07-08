package org.qiunet.utils.http;

import org.apache.http.concurrent.FutureCallback;

import java.util.Map;

public abstract class Callback implements FutureCallback<String> {
	private String url;
	private Map<String, Object> params;
	private Map<String, Object> cookies;

	public void setData(String url, Map<String, Object> params, Map<String, Object> cookies) {
		this.url = url;
		this.params = params;
		this.cookies = cookies;
	}

	@Override
	public void failed(Exception ex) {
		BaseHttpUtil.errorHandler(params, cookies, 0, url, ex);
		this.fail(ex);
	}

	/**
	 * 如果需要callback处理异常 请覆盖该方法
	 * @param ex
	 */
	protected void fail(Exception ex) {}

	@Override
	public void cancelled() {

	}
}
