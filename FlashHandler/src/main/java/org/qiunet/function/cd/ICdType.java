package org.qiunet.function.cd;

import java.util.concurrent.TimeUnit;

/***
 * 业务中, 需要用到cd的地方 , 都定义一个cdType
 * 业务使用一个枚举实现该接口.
 *
 * @author qiunet
 * 2020-03-02 10:50
 ***/
public interface ICdType {
	/**
	 * N秒 能进行几次
	 * @return 次数
	 */
	default int limitCount() {
		return 1;
	}

    /***
	 * 必须间隔多长时间.
	 * @return 时长
	 */
	long period();
	/**
	 * 间隔时间和初始时间的单位
	 * @return 单位
	 */
	TimeUnit unit();
	/**
	 * 描述.
	 * @return 描述
	 */
	String desc();
}
