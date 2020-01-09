package org.qiunet;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.qiunet.frame.RootController;
import org.qiunet.frame.setting.SettingManager;

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
			primaryStage.setOnCloseRequest(event -> SettingManager.getInstance().syncSetting());
		} catch (Exception e) {
			System.exit(1);
		}
	}
}
