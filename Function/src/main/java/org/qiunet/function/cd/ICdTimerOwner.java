package org.qiunet.function.cd;

import java.util.concurrent.TimeUnit;

/***
 * cd管理接口
 *
 * @author qiunet
 * 2021/11/16 17:18
 */
public interface ICdTimerOwner<CdType extends Enum<CdType> & ICdType> {
	/**
	 * 获得cdTimer
	 * @return
	 */
	CdTimer<CdType> getCdTimer();

	/**
	 * 使用指定间隔 刷新指定类型的cd
	 * @param cdType 类型
	 * @param period 间隔
	 * @param unit 间隔单位
	 */
	default void recordCd(CdType cdType, long period, TimeUnit unit) {
		getCdTimer().recordCd(cdType, period, unit);
	}
	/**
	 * 使用CdType枚举默认间隔 刷新指定类型的cd
	 * @param cdType 类型
	 */
	default void recordCd(CdType cdType) {
		getCdTimer().recordCd(cdType);
	}

	/**
	 * 指定类型的cd是否已经过期
	 * @param cdType 类型
	 * @return
	 */
	default boolean isCdTimeOut(CdType cdType) {
		return getCdTimer().isTimeout(cdType);
	}

	/**
	 * 获得剩余的cd时间 秒数
	 * @param cdType cd类型
	 * @return 秒数
	 */
	default int getLeftSeconds(CdType cdType) {
		return getCdTimer().getLeftSeconds(cdType);
	}
	/**
	 * 得到下次cd的毫秒时间戳
	 *
	 * @param cdType
	 * @return 没有cd中, 返回0
	 */
	default long getNextTime(CdType cdType) {
		return getCdTimer().getNextTime(cdType);
	}

	/**
	 * 清除cd
	 * @param cdType cd类型
	 */
	default void cleanCd(CdType cdType) {
		getCdTimer().cleanCd(cdType);
	}
}
