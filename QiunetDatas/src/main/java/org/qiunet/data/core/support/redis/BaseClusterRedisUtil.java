package org.qiunet.data.core.support.redis;

import com.google.common.collect.Sets;
import org.qiunet.listener.hook.ShutdownHookUtil;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.string.StringUtil;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Set;

/***
 * 集群模式的redisUtil
 *
 * @author qiunet
 * 2020-09-29 17:21
 */
public class BaseClusterRedisUtil extends BaseRedisUtil {

	private final JedisCluster jedisCluster;

	public BaseClusterRedisUtil(IKeyValueData<String, String> redisConfig, String redisName) {
		super(redisName);

		Set<HostAndPort> nodes = Sets.newHashSet();
		String nodesString = redisConfig.getString(getConfigKey("nodes"));
		String[] nodesStrings = StringUtil.split(nodesString, ";");
		for (String string : nodesStrings) {
			String[] strings = StringUtil.split(string, ":");
			nodes.add(new HostAndPort(strings[0], Integer.parseInt(strings[1])));
		}
		String password = redisConfig.getString(getConfigKey("pass"), null);
		if ("".equals(password)) password = null;

		int timeout = redisConfig.getInt(getConfigKey("timeout"), 2000);
		int maxAttempts = redisConfig.getInt(getConfigKey("maxAttempts"), 1000);
		jedisCluster = new JedisCluster(nodes, timeout, timeout, maxAttempts, password, this.buildPoolConfig(redisConfig));
		ShutdownHookUtil.getInstance().addShutdownHook(() -> {
			try {
				jedisCluster.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public <T> T execCommands(IRedisCaller<T> caller) {
		NormalJedisProxy handler = new NormalJedisProxy(jedisCluster, caller.log());
		JedisCommands jc = (JedisCommands) Proxy.newProxyInstance(handler.getClass().getClassLoader(), JEDIS_INTERFACES, handler);
		return caller.call(jc);

	}

	@Override
	public JedisCommands returnJedis(boolean log) {
		NormalJedisProxy handler = new NormalJedisProxy(jedisCluster, log);
		return (JedisCommands) Proxy.newProxyInstance(handler.getClass().getClassLoader(), JEDIS_INTERFACES, handler);
	}

}
