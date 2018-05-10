package org.qiunet.data.db.util;

import org.qiunet.data.db.datasource.DataSourceType;
import org.qiunet.utils.properties.LoaderProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  数据库的工具类
 *  支持数据从properties加载
 * @author qiunet
 *         Created on 17/1/5 15:07.
 */
public class DbProperties extends LoaderProperties {
	private static final String KEY_UID_DB_LENGTH = "uid_db_length";
	private static final String KEY_LOGIN_NEED_DB = "login_need_db";
	private static final String KEY_DB_MAX_COUNT = "db_max_count";
	private static final String KEY_DB_NAME_PREFIX="db_name_prefix";
	private static final String KEY_DB_DBCOUNT_FOR_SAME_DATASOURCE="db_size_per_instance";
	private static List<Integer> dbIndexs;
	private int uid_db_factor;
	private String db_name_prefix;
	private int db_size_per_instance;
	/**	 * 玩家数据需要拆表，拆分表的个数	 */
	private static final int PALYER_DATA_TB_DISTRIBUTE_CNT=10;
	private int login_need_db;
	/**	 * 玩家库支持的最大个数	 */
	private int db_max_count;


	private DbProperties() {
		super("db.properties");
		instance = this;
	}
	private volatile static DbProperties instance;
	public static DbProperties getInstance() {
		if (instance == null)  new DbProperties();
		return instance;
	}

	@Override
	protected void onReloadOver() {
		this.db_max_count = getInt(KEY_DB_MAX_COUNT);
		this.login_need_db = db_max_count;
		if (getInt(KEY_LOGIN_NEED_DB) != 0) {
			this.login_need_db = Math.min(db_max_count, getInt(KEY_LOGIN_NEED_DB));
		}
		this.db_name_prefix = getString(KEY_DB_NAME_PREFIX);
		this.db_size_per_instance = getInt(KEY_DB_DBCOUNT_FOR_SAME_DATASOURCE);
		this.uid_db_factor = (int) Math.pow(10, getInt(KEY_UID_DB_LENGTH));
	}

	/**
	 * 得到dbMaxCount
	 * @return 最大db数
	 */
	public int getDbMaxCount(){
		return db_max_count;
	}

	/***
	 * 得到login的hash 用的的db数
	 * @return
	 */
	public int getLoginNeedDb(){
		return login_need_db;
	}
	/**
	 * 返回db前缀
	 * @return db前缀
	 */
	public String getDbNamePrefix(){
		return db_name_prefix;
	}
	/***
	 * 返回每个实例几个库
	 * @return  每个实例几个库的数
	 */
	public int getDbSizePerInstance(){
		return db_size_per_instance;
	}
	/**
	 * 得到玩家库的分表述
	 * @return 分几个表
	 */
	public int getPalyerDataTbDistributeCnt(){
		return PALYER_DATA_TB_DISTRIBUTE_CNT;
	}
	/**
	 * 获取数据库名称
	 * @param dbIndex 数据库索引
	 * @return 得到dbName
	 */
	public String getDbNameByDbIndex(int dbIndex){
		return db_name_prefix + dbIndex;
	}
	/**
	 * 获取数据源名称
	 * @param dbIndex db的库 index
	 * @return 得到dbSource
	 */
	public String getDataSourceTypeByDbIndex(int dbIndex){
		int tmp = (dbIndex / db_size_per_instance) * db_size_per_instance;
		return DataSourceType.DATASOURCE_PLAYER+tmp;
	}
	/**
	 * 返回库索引列表
	 * @return 所有的库index 列表
	 */
	public List<Integer> getDbIndexList(){
		if (dbIndexs == null ) {
			List<Integer> dbIndexsTemp = new ArrayList<Integer>(db_max_count);
			for (int i = 0  ; i < db_max_count; i++) dbIndexsTemp.add(i);
			Collections.shuffle(dbIndexsTemp);
			dbIndexs = Collections.unmodifiableList(dbIndexsTemp);
		}
		return dbIndexs;
	}

	/***
	 * 通过库自增id , 得到一个组合有分库信息的id
	 * @param incrId
	 * @param dbIndex
	 * @return
	 */
	public int buildDbInfoId(int incrId, int dbIndex) {
		if (incrId == 0) throw new IllegalArgumentException("incrId can be zero!");
		return incrId * uid_db_factor + dbIndex;
	}

	/***
	 * 通过一个合成id 得到真实id
	 * @param buildId
	 * @return
	 */
	public int getRealId(int buildId) {
		return buildId / uid_db_factor;
	}
	/***
	 * 得到dbIndex
	 *
	 * @param uid
	 * @return
	 */
	public int getDbIndexByUid(int uid ){
		return getDbIndexById(uid);
	}

	/***
	 * 得到tbIndex
	 * @param uid
	 * @return
	 */
	public int getTbIndexByUid(int uid ){
		return getTbIndexById(uid);
	}

	/***
	 * 是否是有效的ID
	 * @param id
	 * @return
	 */
	public boolean isValidId(int id) {
		return id / uid_db_factor != 0 && Math.abs(id % uid_db_factor) < db_max_count;
	}
	/***
	 * 得到dbIndex
	 *
	 * @param id
	 * @return
	 */
	public int getDbIndexById(int id ){
		return (Math.abs(id) % uid_db_factor);
	}

	/***
	 * 得到tbIndex
	 * @param id
	 * @return
	 */
	public int getTbIndexById(int id ){
		return (Math.abs(id) / uid_db_factor) % PALYER_DATA_TB_DISTRIBUTE_CNT;
	}
}
