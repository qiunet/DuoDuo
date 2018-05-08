package org.qiunet.test.response.annotation.support;

import org.qiunet.test.response.ILongConnResponse;
import org.qiunet.utils.exceptions.SingletonException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiunet.
 * 17/12/6
 */
public class ResponseMapping {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	private volatile static ResponseMapping instance;
	private Map<Integer, ILongConnResponse> gameResponses = new HashMap<>();

	private ResponseMapping() {
		synchronized (ResponseMapping.class) {
			if (instance != null ){
				throw new SingletonException("RequestHandlerMapping instance was duplicate");
			}
			instance = this;
		}
	}

	public static ResponseMapping getInstance() {
		if (instance == null) {
			synchronized (ResponseMapping.class) {
				if (instance == null)
				{
					new ResponseMapping();
				}
			}
		}
		return instance;
	}

	/**
	 * 存一个handler对应mapping
	 * @param responseId
	 * @param response
	 */
	public void addResponse(int responseId, ILongConnResponse response) {
		if (this.gameResponses.containsKey(responseId)) {
			throw new RuntimeException("responseId ["+responseId+"] is already exist!");
		}
		this.gameResponses.put(responseId, response);
	}

	public ILongConnResponse getResponse(int responseId) {
		if (!gameResponses.containsKey(responseId)) {
			logger.error("Response ID ["+responseId+"] was not define");
		}
		return gameResponses.get(responseId);
	}
}
