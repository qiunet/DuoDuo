package org.qiunet.flash.handler.gamecfg;

import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.properties.LoaderProperties;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 总管 游戏设定加载
 * @author qiunet
 *         Created on 17/2/9 12:15.
 */
public class GameCfgManagers {
	private Logger qLogger = LoggerFactory.getLogger(LoggerType.DUODUO);

	private static GameCfgManagers instance = new GameCfgManagers();
	private List<GameSettingContainer<IGameCfgManager>> gameSettingList = new ArrayList<>();
	private List<GameSettingContainer<LoaderProperties>> propertylist = new ArrayList<>();

	private GameCfgManagers(){
		if (instance != null) {
			throw new IllegalStateException("Already has instance .");
		}
	}

	public static GameCfgManagers getInstance(){
		if (instance == null) {
			synchronized (GameCfgManagers.class){
				new GameCfgManagers();
			}
		}
		return instance;
	}

	/**
	 * 初始化会比重新加载多一层排序
	 */
	public void initSetting() throws Exception{
		Collections.sort(gameSettingList);
		Collections.sort(propertylist);
		this.reloadSetting();
	}

	/***
	 * 重新加载
	 * @return 返回加载失败的文件名称
	 * @throws Exception
	 */
	public List<String> reloadSetting() throws  Exception{
		qLogger.error("Game Setting Data Load start.....");
		this.loadPropertySetting();
		List<String> failFileNames = this.loadDataSetting();
		qLogger.error("Game Setting Data Load over.....");
		return failFileNames;
	}

	/**
	 * 添加 Manager
	 * @param manager
	 * @param order
	 */
	public void addDataSettingManager(IGameCfgManager manager, int order) {
		this.gameSettingList.add(new GameSettingContainer(manager, order));
	}
	/**
	 * 添加 properties
	 * @param properties
	 * @param order
	 */
	public void addPropertySetting(LoaderProperties properties, int order) {
		this.propertylist.add(new GameSettingContainer(properties, order));
	}
	/**
	 * 加载property
	 */
	protected void loadPropertySetting() {
		for (GameSettingContainer<? extends LoaderProperties> container : propertylist){
			container.t.reload();
		}
	}

	/***
	 * 加载设定文件
	 * @return 返回加载失败的文件名称
	 * @throws Exception
	 */
	protected List<String> loadDataSetting() throws  Exception{
		List<String> failFileNames = new ArrayList<>(5);
		for (GameSettingContainer<IGameCfgManager> container : gameSettingList) {
			String name = container.t.loadCfg();
			if(!StringUtil.isEmpty(name)) {
				failFileNames.add(name);
			}
		}
		return failFileNames;
	}

	/***
	 * 得到cfg的数量
	 * @return
	 */
	public int cfgSize(){
		return this.gameSettingList.size();
	}

	/***
	 * 得到properties的数量
	 * @return
	 */
	public int propertySize(){
		return propertylist.size();
	}
	/***
	 * IGameSetting  的包装类 包含排序
	 * @param <T>
	 */
	private static class GameSettingContainer<T> implements Comparable<GameSettingContainer<T>> {
		private T t;
		private int order ;

		public GameSettingContainer(T t , int order ){
			this.order = order;
			this.t = t;
		}

		@Override
		public int compareTo(GameSettingContainer<T> o) {
			return o.order - order;
		}
	}
}
