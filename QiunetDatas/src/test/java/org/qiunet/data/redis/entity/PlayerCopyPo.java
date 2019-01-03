package org.qiunet.data.redis.entity;

import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.utils.common.CommonUtil;

import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/1/5 11:25.
 */
public class PlayerCopyPo extends PlayerPo {
	public enum FieldEnum implements IField {
		level,
		nick,
		exp
	}
	private String nick = "";

	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

	public IField[] getFields() {
		return FieldEnum.values();
	}
}
