package org.qiunet.excel2cfgs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.qiunet.excel2cfgs.frame.RootController;
import org.qiunet.excel2cfgs.frame.setting.SettingManager;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * qiunet
 * 2019-11-05 16:37
 ***/
public class Application extends javafx.application.Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {

			FXMLLoader loader = new FXMLLoader(Application.class.getResource("/ExcelToCfgs.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setTitle("配置文件转换工具");
			primaryStage.setWidth(1053);
			primaryStage.setHeight(680);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			RootController.getInstance().init(primaryStage);
		} catch (Exception e) {
			LoggerType.DUODUO.error("Exception: ", e);
			System.exit(1);
		}finally {
			primaryStage.setOnCloseRequest(event -> {
				SettingManager.getInstance().syncSetting();
				// 其它需要停止的代码
			});
		}
	}
}
