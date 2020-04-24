package org.qiunet.cfg.listener;

import org.qiunet.utils.listener.EventListener;
import org.qiunet.utils.listener.IEventData;

/***
 *
 *
 * @author qiunet
 * 2020-04-24 11:25
 ***/
@EventListener(CfgLoadCompleteEventData.CfgLoadCompleteListener.class)
public class CfgLoadCompleteEventData implements IEventData {

	public interface CfgLoadCompleteListener {
		void loadComplete(CfgLoadCompleteEventData data);
	}
}
