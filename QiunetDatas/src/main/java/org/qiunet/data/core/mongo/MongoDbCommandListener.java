package org.qiunet.data.core.mongo;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 * @author qiunet
 * 2023/8/23 10:18
 */
enum MongoDbCommandListener implements CommandListener {
	instance;

	@Override
	public void commandStarted(CommandStartedEvent event) {
		LoggerType.DUODUO_SQL.info("[MongoDB Command:{}] :: {}", event.getCommandName(), event.getCommand());
	}

	@Override
	public void commandSucceeded(CommandSucceededEvent event) {
		//LoggerType.DUODUO_SQL.info("[MongoDB Result:{}] :: {}", event.getCommandName(), event.getResponse());
	}

	@Override
	public void commandFailed(CommandFailedEvent event) {
		LoggerType.DUODUO_SQL.error("[MongoDB fail]{} :: {}", event.getCommandName(), event.getThrowable().getMessage());
	}
}
