package org.qiunet.data.db.datasource;

public enum DbSourceType {
	/**玩家库*/
	DATASOURCE_PLAYER(""),
	/**全局库*/
	DATASOURCE_GLOBAL("global"),
	;
	private String keyPrefix;
	DbSourceType(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	/***
	 * 有分库的使用这个方法获取源
	 * @param dbSourceIndex db源的Index
	 * @return
	 */
	public String getPlayerDbSourceKey(int  dbSourceIndex) {
		if (this == DbSourceType.DATASOURCE_GLOBAL) {
			throw new RuntimeException("can not access this method for global db");
		}
		return String.valueOf(dbSourceIndex);
	}

	/***
	 * 得到global库源的key
	 * @return
	 */
	public String getGlobalDbSourceKey(){
		if (this == DbSourceType.DATASOURCE_PLAYER) {
			throw new RuntimeException("can not access this method for player db");
		}
		return this.keyPrefix;
	}
}
