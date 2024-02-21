package org.qiunet.utils.scanner;

/***
 * 扫描类型.
 * 可以细化到每个 Application 也可以组装.
 *
 * @author qiunet
 * 2020-04-25 20:11
 **/
public enum  ScannerType {
	NONE(0),
	/**str -> 对象转换*/
	OBJ_CONVERT(1 << 1),
	/**事件*/
	EVENT(1 << 2),
	/**条件**/
	CONDITION( 1 << 3),
	/**压测机器人行为树*/
	ROBOT_BEHAVIOR_BUILDER(1 << 4),
	/**配置读取*/
	CFG_SCANNER(1 << 5),
	/**自动注入*/
	AUTO_WIRE(1 << 6, OBJ_CONVERT, EVENT),
	/**文件配置*/
	FILE_CONFIG(1 << 7, OBJ_CONVERT),
	/**行为树action节点*/
	BEHAVIOR_ACTION(1 << 8, CONDITION),
	/**AI 策划用配置文件生成*/
	CREATE_AI_CONFIG(1 << 9),
	/**数据库数据检查*/
	DATABASE(1 << 10, FILE_CONFIG),
	/**定时调度*/
	CRON(1 << 11),
	/**RPC*/
	RPC(1 << 12),
	/**埋点日志*/
	LOG_RECORD(1 << 13, FILE_CONFIG),
	/**公式**/
	FORMULA(1 << 14, AUTO_WIRE, EVENT),
	/**通道数据. 请求*/
	CHANNEL_DATA(1 << 15),
	/**gm命令**/
	GM_COMMAND(1 << 16),
	/***key val 配置*/
	KEY_VAL_CFG(1 << 17, CFG_SCANNER),
	/**生成proto文件*/
	GENERATOR_PROTO(1 << 18, CHANNEL_DATA),
	/**响应*/
	GAME_TEST_RESPONSE(1 << 19),
	/**服务节点*/
	SERVER_NODE(1 << 20, EVENT),
	/**任务处理*/
	TARGET_HANDLER(1 << 21, EVENT),
	/***远程数据调用*/
	RDC(1 << 22),
	/***url类型请求*/
	URL_REQUEST(1 << 23),
	/** 属性*/
	ATTR(1 << 24),
	/**假枚举*/
	FAKE_ENUM(1 << 25),

	/**配置读取*/
	CFG (
		EVENT,
		AUTO_WIRE,
		OBJ_CONVERT,
		CFG_SCANNER,
		KEY_VAL_CFG
	),
	/** 仅服务端 */
	SERVER(
		CFG,
		RPC,
		RDC,
		ATTR,
		CRON,
		FORMULA,
		DATABASE,
		FAKE_ENUM,
		CONDITION,
		GM_COMMAND,
		LOG_RECORD,
		FILE_CONFIG,
		SERVER_NODE,
		URL_REQUEST,
		CHANNEL_DATA,
		TARGET_HANDLER,
		BEHAVIOR_ACTION
	),
	/** 客户端使用 */
	CLIENT (
		CFG,
		FORMULA,
		CONDITION,
		CHANNEL_DATA
		),

	/**压测*/
	GAME_TEST(SERVER,
		GAME_TEST_RESPONSE,
		ROBOT_BEHAVIOR_BUILDER),

	/** 所有 */
	ALL(Long.MAX_VALUE),
	;

	private final long originStatus;
	private final long complexStatus;
	ScannerType(long status, ScannerType ... types) {
		long complexStatus = status;
		for (ScannerType type : types) {
			complexStatus |= type.complexStatus;
		}
		this.complexStatus = complexStatus;
		this.originStatus = status;
	}

	ScannerType(ScannerType ... types) {
		this(0, types);
	}

	ScannerType(long status) {
		this.originStatus = status;
		this.complexStatus = status;
	}

	/**
	 * 复合status
	 * @return
	 */
	public long getStatus() {
		return complexStatus;
	}

	/***
	 * 是否可以扫描
	 * @return
	 */
	public boolean test(long val) {
		return (originStatus | val) == val;
	}
}
