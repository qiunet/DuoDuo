package org.qiunet.excel2cfgs.listener;

import org.qiunet.utils.listener.EventListener;
import org.qiunet.utils.listener.IEventData;

/***
 * svn 处理状态改变事件数据
 * @author qiunet
 * 2020-02-02 15:05
 **/
@EventListener(SvnProcessingListenerData.SvnProcessingListener.class)
public class SvnProcessingListenerData implements IEventData {

	public interface SvnProcessingListener {
		void onSvnProcessing(SvnProcessingListenerData data);
	}
}
