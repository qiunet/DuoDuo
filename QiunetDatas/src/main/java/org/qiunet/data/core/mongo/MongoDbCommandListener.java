package org.qiunet.data.core.mongo;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import org.bson.BsonDocument;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;

import java.util.concurrent.TimeUnit;

/***
 * mongodb 命令打印
 * @author qiunet
 * 2023/8/23 10:18
 */
enum MongoDbCommandListener implements CommandListener {
	instance;
	private static final ThreadLocal<String> local = new ThreadLocal<>();

	@Override
	public void commandStarted(CommandStartedEvent event) {
		BsonValue bsonValue;
		BsonDocument bsonDocument = event.getCommand();
		if ((bsonValue = bsonDocument.get(event.getCommandName())) == null) {
			return;
		}

		if (bsonValue.getBsonType() != BsonType.STRING) {
			return;
		}

		String collectionName = bsonValue.asString().getValue();
		if (collectionName.startsWith("__")) {
			return;
		}

		BsonValue param  = bsonDocument;
		switch (event.getCommandName()) {
			case "update":
				param = bsonDocument.getArray("updates").get(0);
				break;
			case "delete":
				param = bsonDocument.getArray("deletes").get(0);
				break;
			case "aggregate":
				param = bsonDocument.get("pipeline");
				break;
			case "find":
				param = bsonDocument.get("filter");
				break;
		}
		BsonValue params = param.isArray() ? param.asArray() : param;
		String format = StringUtil.slf4jFormat("[Mongo {}.{}.{}] {}", event.getDatabaseName(), collectionName, event.getCommandName(), params.toString());
		local.set(format);
	}

	@Override
	public void commandSucceeded(CommandSucceededEvent event) {
		try {
			String printInfo = local.get();
			if (StringUtil.isEmpty(printInfo)) {
				return;
			}
			LoggerType.DUODUO_SQL.info("{}ms {}",event.getElapsedTime(TimeUnit.MILLISECONDS), printInfo);
		}finally {
			local.remove();
		}
	}

	@Override
	public void commandFailed(CommandFailedEvent event) {
		LoggerType.DUODUO_SQL.error("[MongoDB fail]{} :: {}", event.getCommandName(), event.getThrowable().getMessage());
	}
}
