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

	public Excel2CfgServerStartListenerData() {
	}

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final Excel2CfgServerStartListenerData instance = new Excel2CfgServerStartListenerData();

	public static void fireStartEventHandler(){
		instance.fireEventHandler();
	}

	public Excel2CfgServerStartListenerData(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}

}
