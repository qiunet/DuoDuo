package org.qiunet.function.badword;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 *
 * 加载bad word
 *
 * @author qiunet
 * 2021/11/25 10:35
 */
public class LoadBadWordEvent implements IListenerEvent {
	/**
	 * 加载数据
	 */
	private IBadWord badWord;

	public static LoadBadWordEvent valueOf(IBadWord badWord){
		LoadBadWordEvent data = new LoadBadWordEvent();
		data.badWord = badWord;
		return data;
	}

	public IBadWord getBadWord() {
		return badWord;
	}
}
