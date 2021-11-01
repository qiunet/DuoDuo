package org.qiunet.data.core.support.redis;

import redis.clients.jedis.commands.JedisCommands;

/***
 * 集群模式的redisUtil
 * 目前知道的集群貌似不需要业务关心. 都是云实现无感集群.
 *
 * @author qiunet
 * 2020-09-29 17:21
 */
public class BaseClusterRedisUtil extends BaseRedisUtil {

	BaseClusterRedisUtil(String redisName) {
		super(redisName);
	}

	@Override
	public <T> T execCommands(IRedisCaller<T> caller) {
		return null;
	}

	@Override
	public JedisCommands returnJedis(boolean log) {
		return null;
	}
//
//	private final JedisCluster jedisCluster;
//
//	public BaseClusterRedisUtil(IKeyValueData<String, String> redisConfig, String redisName) {
//		super(redisName);
//
//		Set<HostAndPort> nodes = Sets.newHashSet();
//		String nodesString = redisConfig.getString(getConfigKey("nodes"));
//		String[] nodesStrings = StringUtil.split(nodesString, ";");
//		for (String string : nodesStrings) {
//			String[] strings = StringUtil.split(string, ":");
//			nodes.add(new HostAndPort(strings[0], Integer.parseInt(strings[1])));
//		}
//		String password = redisConfig.getString(getConfigKey("pass"), null);
//		if ("".equals(password)) password = null;
//
//		int timeout = redisConfig.getInt(getConfigKey("timeout"), 2000);
//		int maxAttempts = redisConfig.getInt(getConfigKey("maxAttempts"), 1000);
//		jedisCluster = new JedisCluster(nodes, timeout, timeout, maxAttempts, password, this.buildPoolConfig(redisConfig));
//		ShutdownHookUtil.getInstance().addShutdownHook(() -> {
//			try {
//				jedisCluster.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
//	}
//
//	@Override
//	public <T> T execCommands(IRedisCaller<T> caller) {
////		NormalJedisProxy handler = new NormalJedisProxy(jedisCluster, caller.log());
//		JedisCommands jc = (JedisCommands) Proxy.newProxyInstance(handler.getClass().getClassLoader(), JEDIS_INTERFACES, handler);
//		return caller.call(jc);
//
//	}
//
//	@Override
//	public JedisCommands returnJedis(boolean log) {
//		NormalJedisProxy handler = new NormalJedisProxy(jedisCluster, log);
//		return (JedisCommands) Proxy.newProxyInstance(handler.getClass().getClassLoader(), JEDIS_INTERFACES, handler);
//	}

}
