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
	/***
	 * 使用自己指定的period 记录cd 并返回cd是否失效.
	 * 有时候不一定是使用 cdType 定义的间隔时间.
	 * @param cdType cd 类型
	 * @param period 自己指定的间隔时间
	 * @param unit 时间单位
	 * @param limitCount 单位时间能进行几次
	 */
	default void recordCd(CdType cdType, long period, TimeUnit unit, int limitCount) {
		getCdTimer().recordCd(cdType, period, unit, limitCount);
	}
	/**
	 * 使用指定间隔 刷新指定类型的cd
	 * @param cdType 类型
	 * @param period 间隔
	 * @param unit 间隔单位
	 */
	default void recordCd(CdType cdType, long period, TimeUnit unit) {
		this.recordCd(cdType, period, unit, 1);
	}
	/**
	 * 使用CdType枚举默认间隔 刷新指定类型的cd
	 * @param cdType 类型
	 */
	default void recordCd(CdType cdType, long period) {
		this.recordCd(cdType, period, cdType.unit());
	}
	/**
	 * 使用CdType枚举默认间隔 刷新指定类型的cd
	 * @param cdType 类型
	 */
	default void recordCd(CdType cdType) {
		this.recordCd(cdType, cdType.period());
	}

	/**
	 * 指定类型的cd是否已经过期
	 * @param cdType 类型
	 * @return true 不在cd中
	 */
	default boolean isCdTimeOut(CdType cdType) {
		return getCdTimer().isTimeout(cdType);
	}

	/**
	 * 是否在cd中
	 * @param cdType
	 * @return true cd中
	 */
	default boolean isCding(CdType cdType) {
		return ! isCdTimeOut(cdType);
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
