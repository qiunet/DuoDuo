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
		private int status;
		private String desc;

		public HttpStatus() {}

		public HttpStatus(IGameStatus gameStatus) {
			this(gameStatus.getStatus(), gameStatus.getDesc());
		}

		public HttpStatus(int status, String desc) {
			this.status = status;
			this.desc = desc;
		}

		public int getStatus() {
			return status;
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
	 * 设置
	 * @param status
	 */
	void setStatus(HttpStatus status);
}
