package org.qiunet.fx.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.qiunet.fx.AppMain;
import org.qiunet.fx.common.ConfigManager;
import org.qiunet.fx.controller.BaseController;
import org.qiunet.utils.ExecutorServiceUtil;


/**
 * created by wgw on 2019/7/28
 */
public class AppMainStage extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(AppMainStage.class.getResource(AppMain.fxml_path + "AppMain.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root, 600, 400);
			primaryStage.setTitle("game_tool");
			primaryStage.setScene(scene);
			primaryStage.setMinHeight(600);
			primaryStage.setMinWidth(900);
			primaryStage.show();
			ExecutorServiceUtil.getInstance().submit(() -> {
				ConfigManager.getInstance();
			});

			BaseController controller = loader.getController();
			controller.init(primaryStage);
			//关闭监听
			primaryStage.setOnCloseRequest((event) -> {
				ExecutorServiceUtil.getInstance().shutdown();
			});
		} catch (Exception e) {
			ExecutorServiceUtil.getInstance().shutdown();
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void run(String[] args) {
		Application.launch(args);
	}
}
