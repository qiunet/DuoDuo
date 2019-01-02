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
		LEVEL("level"),
		NICK("nick"),
		EXP("exp"),
		;
		private String fieldName;
		FieldEnum(String fieldName) {
			this.fieldName = fieldName;
		}
		@Override
		public String getFieldName() {
			return fieldName;
		}
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
