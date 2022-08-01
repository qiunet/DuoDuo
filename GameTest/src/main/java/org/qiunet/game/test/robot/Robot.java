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

	public Robot(String account, int tickMillis, boolean printLog) {
		super(tickMillis, printLog);
		this.account = account;
	}

	public Robot(String account, int tickMillis) {
		this(account, tickMillis, false);
	}

	public Robot(String account, boolean printLog) {
		this(account, 500, printLog);
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
	public void auth(long id) {
		// do nothing
	}

	@Override
	public String msgExecuteIndex() {
		return this.account;
	}
}
