package org.qiunet.function.badword;

import java.util.List;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/27 19:22
 **/
public interface IBadWord {
	/***
	 * 得到过滤字列表
	 * @return
	 */
	List<String> getBadWordList();
}
