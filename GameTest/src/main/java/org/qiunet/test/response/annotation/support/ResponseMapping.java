package org.qiunet.test.response.annotation.support;

import org.qiunet.test.response.IPersistConnResponse;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiunet.
 * 17/12/6
 */
public enum  ResponseMapping {
	instance;
	private Logger logger = LoggerType.DUODUO_GAME_TEST.getLogger();
	private Map<Integer, IPersistConnResponse> gameResponses = new HashMap<>();

	/**
	 * 存一个handler对应mapping
	 * @param responseId
	 * @param response
	 */
	void addResponse(int responseId, IPersistConnResponse response) {
		if (this.gameResponses.containsKey(responseId)) {
			throw new CustomException("responseId ["+responseId+"] is already exist!");
		}
		this.gameResponses.put(responseId, response);
	}

	public IPersistConnResponse getResponse(int responseId) {
		if (!gameResponses.containsKey(responseId)) {
			logger.error("Response ID ["+responseId+"] was not define");
		}
		return gameResponses.get(responseId);
	}
}
