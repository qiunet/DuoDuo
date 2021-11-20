package org.qiunet.utils.test.jmh;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2021/11/20 16:26
 */
@ProtobufClass
public class PlayerObj {
	/**
	 * id
	 *
	 */
	@Protobuf
	private long id;
	/**
	 * 等级
	 */
	@Protobuf
	private int level;
	/**
	 * 经验
	 */
	@Protobuf
	private long exp;
	/**
	 * 名称
	 */
	@Protobuf
	private String name;
	/**
	 * 简介
	 */
	@Protobuf
	private String introduction;
	/**
	 * 背包
	 */
	@Protobuf
	private List<ItemObj> pack;

	public PlayerObj() {}

	public PlayerObj(long id, int level, long exp, String name, String introduction, List<ItemObj> pack) {
		this.id = id;
		this.level = level;
		this.exp = exp;
		this.name = name;
		this.introduction = introduction;
		this.pack = pack;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public List<ItemObj> getPack() {
		return pack;
	}

	public void setPack(List<ItemObj> pack) {
		this.pack = pack;
	}
}
