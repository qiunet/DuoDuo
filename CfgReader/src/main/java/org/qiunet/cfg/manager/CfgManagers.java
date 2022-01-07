package org.qiunet.cfg.manager;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import org.qiunet.cfg.listener.CfgLoadCompleteEventData;
import org.qiunet.cfg.listener.CfgManagerAddEventData;
import org.qiunet.cfg.manager.base.ICfgManager;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 总管 游戏设定加载
 * @author qiunet
 *         Created on 17/2/9 12:15.
 */
public enum CfgManagers {
	INSTANCE,;
	private final List<ICfgManager> gameSettingList = Lists.newArrayListWithCapacity(100);
	private final Logger logger = LoggerType.DUODUO_CFG_READER.getLogger();
	private final AtomicBoolean reloading = new AtomicBoolean();

	public static CfgManagers getInstance(){
		return INSTANCE;
	}

	/**
	 * 初始化会比重新加载多一层排序
	 */
	public synchronized void initSetting() {
		gameSettingList.sort(((o1, o2) -> ComparisonChain.start().compare(o2.order(), o1.order()).result()));
		this.reloadSetting(false);
	}

	/***
	 * 重新加载
	 * @return 返回加载失败的文件名称
	 * @throws Exception
	 */
	public synchronized void reloadSetting() {
		this.reloadSetting(true);
	}
	private synchronized void reloadSetting(boolean needLogger) {
		if (reloading.get()) {
			logger.error("Game Setting Data is loading now.....");
			return;
		}

		logger.error("Game Setting Data Load start.....");
		try {
			reloading.compareAndSet(false, true);
			this.loadDataSetting();
		}catch (CustomException e) {
			if (needLogger) {
				e.logger(logger);
			}
			throw e;
		} finally {
			reloading.compareAndSet(true, false);
		}
		logger.error("Game Setting Data Load over.....");
		CfgLoadCompleteEventData.fireEvent();
	}

	/**
	 * 添加 Manager
	 * @param manager
	 */
	public void addCfgManager(ICfgManager manager) {
		CfgManagerAddEventData.fireEvent(manager);
		this.gameSettingList.add(manager);
	}

	/***
	 * 加载设定文件
	 * @return 返回加载失败的文件名称
	 */
	private synchronized void loadDataSetting() {
		int size = gameSettingList.size();
		CountDownLatch latch = new CountDownLatch(size);
		AtomicReference<CustomException> reference = new AtomicReference<>();
		for (ICfgManager cfgManager : gameSettingList) {
			if (cfgManager.order() > 0) {
				try {
					cfgManager.loadCfg();
				}catch (Exception e) {
					throw new CustomException(e, "读取配置文件 [{}]({}) 失败!", cfgManager.getCfgClass().getSimpleName(), cfgManager.getLoadFileName());
				}
				logger.info("Load Config [{}]({})", cfgManager.getCfgClass().getSimpleName(), cfgManager.getLoadFileName());
				latch.countDown();
				continue;
			}

			DFuture<Void> dFuture = TimerManager.executorNow(() -> {
					cfgManager.loadCfg();
					return null;
			});

			dFuture.whenComplete((res, ex) -> {
				if (ex != null) {
					reference.compareAndSet(null, new CustomException(ex, "读取配置文件[{}]失败!", cfgManager.getLoadFileName()));

					for (long i = 0; i < latch.getCount(); i++) {
						latch.countDown();
					}
					return;
				}

				logger.info("Load Config [{}]({})", cfgManager.getCfgClass().getSimpleName(), cfgManager.getLoadFileName());
				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (reference.get() != null) {
			throw reference.get();
		}
	}
}
