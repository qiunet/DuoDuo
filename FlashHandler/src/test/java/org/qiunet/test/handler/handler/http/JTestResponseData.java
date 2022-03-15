package org.qiunet.test.handler.handler.http;

import org.qiunet.flash.handler.context.request.http.json.IResultResponse;

/***
 *
 * @author qiunet
 * 2022/3/15 17:31
 */
public class JTestResponseData implements IResultResponse {

	private HttpStatus status = IResultResponse.SUCCESS;

	private String test;


	public JTestResponseData() {
	}

	public JTestResponseData(String test) {
		this.test = test;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	public String getTest() {
		return test;
	}
}
