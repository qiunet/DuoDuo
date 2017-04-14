package org.qiunet.data.redis.entity;

import org.qiunet.utils.common.CommonUtil;

import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/1/5 11:25.
 */
public class PlayerCopyPo extends PlayerPo {
	
	public static final String FILED_LEVEL = "level";
	public static final String FILED_EXP = "exp";
	public static final String FILED_NICK = "nick";
	
	private static final String [] fields = {FILED_NICK, FILED_EXP, FILED_LEVEL};
	
	private String nick;
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

	protected String[] getFields() {
		return fields;
	}
}
