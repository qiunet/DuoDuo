package org.qiunet.excel2cfgs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.listener.Excel2CfgServerStartListenerData;
import org.qiunet.utils.classScanner.ClassScanner;
import org.qiunet.utils.listener.event_data.ServerShutdownEventData;
import org.qiunet.utils.logger.LoggerType;

import java.io.IOException;

/***
 *
 *
 * qiunet
 * 2019-11-05 16:37
 ***/
public class Application extends javafx.application.Application{
	private static Scene scene;
	public static void main(String[] args) {
		FXMLLoader loader = new FXMLLoader(Application.class.getResource("/ExcelToCfgs.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		scene = new Scene(root);

		ClassScanner.getInstance().scanner();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			primaryStage.setTitle("配置文件转换工具");
			primaryStage.setWidth(1053);
			primaryStage.setHeight(680);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();

			new Excel2CfgServerStartListenerData(primaryStage).fireEventHandler();
		} catch (Exception e) {
			LoggerType.DUODUO.error("Exception: ", e);
			System.exit(1);
		}finally {
			primaryStage.setOnCloseRequest(event -> ServerShutdownEventData.fireShutdownEventHandler());
		}
	}
}
