package org.qiunet.cfg.manager.xd;

import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-01-13 09:11
 ***/
public class XdInfoData {
	/***
	 * 数据行数
	 */
	private int num;
	/***
	 * 列名称
	 */
	private List<String> names;

	 XdInfoData(int num, List<String> names) {
		this.num = num;
		this.names = names;
	}

	public int getNum() {
		return num;
	}

	public List<String> getNames() {
		return names;
	}
}
