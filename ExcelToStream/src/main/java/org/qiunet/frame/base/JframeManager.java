package org.qiunet.frame.base;

import java.util.Map;

import java.util.HashMap;


public class JframeManager {
	private static JframeManager instance;
	/**保存一个全局的jframe map */
	static final  Map<Class<? extends BaseJFrame> ,Object> FRAMEMAP = new HashMap<Class<? extends BaseJFrame>, Object>();
	
	public static JframeManager getInstance(){
		if(instance == null){
			instance = new JframeManager();
		}
		return instance;
	}
	
	public <T extends BaseJFrame> T getJframe(Class<T> c){
		if(FRAMEMAP.containsKey(c)){
			return (T)FRAMEMAP.get(c);
		}
		try {
			return c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <T extends BaseJFrame> void removeJframe(Class<T> c){
		FRAMEMAP.remove(c);
	}
}
