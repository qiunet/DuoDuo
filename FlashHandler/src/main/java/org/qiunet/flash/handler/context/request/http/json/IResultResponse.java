package org.qiunet.flash.handler.context.request.http.json;

import org.qiunet.flash.handler.context.status.IGameStatus;

/***
 *
 * @author qiunet
 * 2022/3/15 17:34
 */
public interface IResultResponse {
	HttpStatus SUCCESS = new HttpStatus(IGameStatus.SUCCESS);

	class HttpStatus {
		private int code;
		private String desc;

		public HttpStatus() {}

		public HttpStatus(IGameStatus gameStatus) {
			this(gameStatus.getStatus(), gameStatus.getDesc());
		}

		public HttpStatus(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	/**
	 * 状态内容
	 * @return
	 */
	HttpStatus getStatus();
	/**
	 * 是否成功
	 * @return
	 */
	default boolean isSuccess() {
		return this.statusCode() == IGameStatus.SUCCESS.getStatus();
	}
	/**
	 * 返回响应码
	 * @return
	 */
	default int statusCode() {
		return getStatus().getCode();
	}
	/**
	 * 设置
	 * @param status
	 */
	void setStatus(HttpStatus status);
}
