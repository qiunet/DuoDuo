package org.qiunet.data.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.data.enums.ServerType;

/***
 *
 *
 * qiunet
 * 2019-08-19 12:31
 ***/
public class TestDbUtil {
	@Test
	public void testTableName(){
		Assertions.assertEquals("guild_member", DbUtil.getDefaultTableName("GuildMemberDo"));
		Assertions.assertEquals("item", DbUtil.getDefaultTableName("ItemDo"));
	}

	@Test
	public void testServerId1(){
		ServerType serverType = ServerType.LOGIC;

		int serverId = serverType.buildServerId( 4);

		Assertions.assertEquals(41, serverId);
		Assertions.assertEquals(serverType, ServerType.getServerType(serverId));
	}

	@Test
	public void testServerId2(){
		ServerType serverType = ServerType.CROSS;

		int serverId = serverType.buildServerId( 4);

		Assertions.assertEquals(42, serverId);

		Assertions.assertEquals(serverType, ServerType.getServerType(serverId));
	}

	@Test
	public void testDefaultTableName(){
		String className = "PlayerLevelDo";
		Assertions.assertEquals("player_level", DbUtil.getDefaultTableName(className));
	}
}
