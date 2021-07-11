package org.qiunet.game.test.robot;

import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;

/***
 * 机器人
 *
 * @author qiunet
 * 2021-07-07 10:42
 */
public class Robot extends RobotFunc implements IArgsContainer {
	/**
	 * 存储各种数据的一个容器.
	 */
	private final ArgsContainer container = new ArgsContainer();
	/**
	 * id
	 */
	private long id;
	/**
	 * 账号
	 */
	private final String account;

	public Robot(String account) {
		super();
		this.account = account;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getAccount() {
		return account;
	}

	public boolean isAuth(){
		return this.id > 0;
	}

	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return container.getArgument(key, computeIfAbsent);
	}
}
