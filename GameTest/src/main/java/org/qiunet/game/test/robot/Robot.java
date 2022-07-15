package org.qiunet.game.test.robot;

/***
 * 机器人
 *
 * @author qiunet
 * 2021-07-07 10:42
 */
public class Robot extends RobotFunc{
	/**
	 * id
	 */
	private long id;
	/**
	 * 账号
	 */
	private final String account;

	public Robot(String account, int tickMillis) {
		super(tickMillis);
		this.account = account;
	}

	public Robot(String account) {
		this(account, 500);
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
	public String msgExecuteIndex() {
		return this.account;
	}
}
