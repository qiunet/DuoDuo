package org.qiunet.game.tests.client.data;

import org.qiunet.game.tests.protocol.proto.Item;
import org.qiunet.game.tests.protocol.proto.LoginInfo;
import org.qiunet.utils.args.ArgumentKey;

import java.util.List;

/***
 * 黑板. 存储机器人数据.
 *
 * qiunet
 * 2021/8/5 10:37
 **/
public interface BlackBoard {
	/**
	 * 登录角色信息
	 */
	ArgumentKey<List<LoginInfo>> loginInfo = new ArgumentKey<>();
	/**
	 * 随机名称信息
	 */
	ArgumentKey<String> randomName = new ArgumentKey<>();
	/**
	 * 背包物品
	 */
	ArgumentKey<List<Item>> items = new ArgumentKey<>();
}
