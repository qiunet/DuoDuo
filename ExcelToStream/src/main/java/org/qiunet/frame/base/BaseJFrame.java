package org.qiunet.frame.base;

import javax.swing.*;

/**
 * Created by qiunet.
 * 17/5/25
 */
public class BaseJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6459384524123613791L;

	public BaseJFrame (){
		JframeManager.FRAMEMAP.put(getClass(), this);
	}
}
