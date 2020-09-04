package org.qiunet.excel2cfgs.listener;

import javafx.stage.Stage;
import org.qiunet.listener.event.IEventData;

/***
 *
 * @author qiunet
 * 2020-02-02 15:11
 **/
public class Excel2CfgServerStartListenerData implements IEventData {

	private Stage stage;

	public Excel2CfgServerStartListenerData(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}

}
