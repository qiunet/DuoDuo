package org.qiunet.handler.gamedata;

import org.qiunet.utils.properties.LoaderProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 总管 游戏设定加载
 * @author qiunet
 *         Created on 17/2/9 12:15.
 */
public class GameSettingManagers {
	private static GameSettingManagers instance;
	private List<GameSettingContainer<IGameDataManager>> gameSettingList = new ArrayList<>();
	private List<GameSettingContainer<LoaderProperties>> propertylist = new ArrayList<>();
	
	private GameSettingManagers(){
		if (instance != null) {
			throw new IllegalStateException("Already has instance .");
		}
		instance = this;
	}
	
	public static GameSettingManagers getInstance(){
		if (instance == null) {
			synchronized (GameSettingManagers.class){
				new GameSettingManagers();
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
	/**
	 * 重新加载
	 */
	public void reloadSetting() throws  Exception{
		this.loadPropertySetting();
		this.loadDataSetting();
	}
	
	/**
	 * 添加 Manager
	 * @param manager
	 * @param order
	 */
	public void addDataSettingManager(IGameDataManager manager, int order) {
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
	protected void loadPropertySetting() throws  Exception{
		for (GameSettingContainer<? extends LoaderProperties> container : propertylist){
			container.t.reload();
		}
	}
	/**
	 * 加载数据设定
	 */
	protected void loadDataSetting() throws  Exception{
		for (GameSettingContainer<IGameDataManager> container : gameSettingList) {
			container.t.load();
		}
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
