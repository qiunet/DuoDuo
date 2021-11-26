package org.qiunet.game.tests.server.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.game.tests.protocol.proto.login.LoginInfo;
import org.qiunet.utils.id.DefaultIdGenerator;
import org.qiunet.utils.id.IdGenerator;

import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 存储玩家数据缓存, 相当于数据库和 Redis.
 * 我们这里是测试. 就不连接数据库 redis了.
 *
 * qiunet
 * 2021/8/20 10:34
 **/
public class ServerData {
	/**
	 *
	 */
	public static final Map<String, List<LoginInfo>> loginInfo = Maps.newConcurrentMap();

	public static final Map<Long, PlayerActor> playerInfo = Maps.newConcurrentMap();

	public static final IdGenerator<Long> playerIdGenerator = new DefaultIdGenerator();
	/**
	 * 已经使用的昵称
	 */
	public static final Set<String> usedNameSets = Sets.newHashSet();

	public static final Set<String> nameSets = Sets.newHashSet("名称1", "名称2", "名称3", "名称4", "名称5", "名称6", "名称7", "名称8", "名称9", "名称10");
}
