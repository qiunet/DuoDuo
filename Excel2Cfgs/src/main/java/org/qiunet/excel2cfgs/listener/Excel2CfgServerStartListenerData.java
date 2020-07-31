package org.qiunet.excel2cfgs.listener;

import javafx.stage.Stage;
import org.qiunet.utils.listener.EventListener;
import org.qiunet.utils.listener.IEventData;

/***
 *
 * @author qiunet
 * 2020-02-02 15:11
 **/
@EventListener(Excel2CfgServerStartListenerData.Excel2CfgServerStartListener.class)
public class Excel2CfgServerStartListenerData implements IEventData {

	private Stage stage;

	public Excel2CfgServerStartListenerData(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}

	public interface Excel2CfgServerStartListener {
		void onServerStart(Excel2CfgServerStartListenerData data);
	}
}
