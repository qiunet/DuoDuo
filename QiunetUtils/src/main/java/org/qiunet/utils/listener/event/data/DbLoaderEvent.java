package org.qiunet.utils.listener.event.data;

import org.qiunet.utils.listener.event.IEventData;

/***
 * 启动db事件
 *
 * @author qiunet
 * 2020-09-05 06:07
 **/
public class DbLoaderEvent implements IEventData {

		private DbLoaderEvent(){}

		/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
		private static final DbLoaderEvent instance = new DbLoaderEvent();

		public static void fireDbLoaderEventHandler(){
			instance.fireEventHandler();
		}
}
