package org.qiunet.function.badword;

import org.qiunet.utils.listener.event.IEventData;

/***
 *
 * 加载bad word
 *
 * @author qiunet
 * 2021/11/25 10:35
 */
public class LoadBadWordEventData implements IEventData {
	/**
	 * 加载数据
	 */
	private IBadWord badWord;

	public static LoadBadWordEventData valueOf(IBadWord badWord){
		LoadBadWordEventData data = new LoadBadWordEventData();
		data.badWord = badWord;
		return data;
	}

	public IBadWord getBadWord() {
		return badWord;
	}
}
