package org.qiunet.data.core.support.redis;

import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.commands.MultiKeyCommands;

/***
 * 需要使用的redis 命令
 *
 * @author qiunet
 * 2021/11/5 15:54
 */
public interface IJedis extends JedisCommands, MultiKeyCommands {
}
