package org.qiunet.fx.controller;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.qiunet.fx.bean.FileTreeItem;
import org.qiunet.fx.bean.GlobalMenu;
import org.qiunet.fx.common.ConfigManager;
import org.qiunet.utils.ExecutorServiceUtil;
import org.qiunet.utils.FileUtil;
import org.qiunet.utils.FxUIUtil;
import org.qiunet.utils.SvnUtil;
import org.qiunet.utils.string.StringUtil;

import java.awt.*;
import java.io.File;
import java.util.*;

/**
 * created by wgw on 2019/7/28
 */
public class AppMainController extends BaseController {

	private Stage stage;
	@FXML
	private RadioButton searchAll;
	@FXML
	private RadioButton searchItem;
	@FXML
	private TextField searchText;
	@FXML
	private CheckBox blurryCheck;
	@FXML
	private ChoiceBox<String> excelPaths;

	private final ToggleGroup searchGroup = new ToggleGroup();

	//信息输出
	@FXML
	private TextArea msgContent;
	@FXML
	private ChoiceBox<String> outPaths;


	@FXML
	private CheckBox checkJson;
	@FXML
	private CheckBox checkXML;
	@FXML
	private CheckBox checkXD;
	@FXML
	private TreeView<File> treeView;

	@Override
	public void init(Object... objs) {
		if (objs.length > 0 && objs[0] instanceof Stage)
			this.stage = (Stage) objs[0];
		/**初始化单选框*/
		initToggleGroup();
		/**初始化下拉选择框*/
		intiChoiceBox();


		addTreeViewListener();
	}



	//加载tree数据
	public void loadTreeData(String filePath) {
		if (StringUtil.isEmpty(filePath))
			return;
		File rootFile = new File(filePath);
		TreeItem<File> rootItem = new TreeItem<>(rootFile);
		for (File file : rootFile.listFiles()) {
			if (!FileUtil.filePostfixCheck(file)) continue;
			FileTreeItem rootsitem = new FileTreeItem(file);
			rootItem.getChildren().add(rootsitem);
		}
		treeView.setRoot(rootItem);
	}

	//添加tree事件监听
	public void addTreeViewListener() {
		GlobalMenu.getInstance().addOnAction(treeView);
		treeView.setContextMenu(GlobalMenu.getInstance());
		treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				MouseButton type=mouseEvent.getButton();
				TreeItem<File> item = treeView.getSelectionModel().getSelectedItem();
				//双击左键
				if (type== MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
					try {
						Desktop.getDesktop().open(item.getValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}


			}
		});
	}


	/***/
	public void intiChoiceBox() {
		Map<String, String> outPathMap = ConfigManager.getInstance().getOut_path();
		Set<String> set = ConfigManager.getInstance().getExcel_path_array();
		if (set != null && !set.isEmpty())
			excelPaths.getItems().addAll(set);
		String last_excel = ConfigManager.getInstance().getLast_check_excel();
		if (!StringUtil.isEmpty(last_excel)) {
			excelPaths.getSelectionModel().select(last_excel);
			loadTreeData(last_excel);
		}
		//下拉选择框事件监听
		excelPaths.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
			if (oldValue.equals(newValue))
				return;
			String path = excelPaths.getItems().get(excelPaths.getSelectionModel().getSelectedIndex());
			boolean isOutHas = outPathMap.containsKey(path);
			if (isOutHas) {
				outPaths.getSelectionModel().select(outPathMap.get(path));
			}
			ExecutorServiceUtil.getInstance().submit(() -> {
				ConfigManager.getInstance().write(ConfigManager.last_check_excel_key, path, false);
			});
			loadTreeData(path);
		}));


		if (outPathMap != null && !outPathMap.isEmpty()) {
			outPaths.getItems().addAll(outPathMap.values());
			if (!StringUtil.isEmpty(last_excel) && !StringUtil.isEmpty(outPathMap.get(last_excel)))
				outPaths.getSelectionModel().select(outPathMap.get(last_excel));
		}

		outPaths.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue.equals(newValue))
				return;
			final String path = outPaths.getItems().get(outPaths.getSelectionModel().getSelectedIndex());
			ExecutorServiceUtil.getInstance().submit(() -> {
				ConfigManager.getInstance().writeOutPath(getChoiceSelect(excelPaths), path);
			});
		});

	}

	public <T> T getChoiceSelect(ChoiceBox<T> choiceBox) {
		if (choiceBox.getSelectionModel().getSelectedIndex() < 0)
			return null;
		return choiceBox.getItems().get(choiceBox.getSelectionModel().getSelectedIndex());
	}

	public void initToggleGroup() {
		searchAll.setToggleGroup(searchGroup);
		searchAll.setSelected(true);
		searchItem.setToggleGroup(searchGroup);
		//单选框事件监听
		searchGroup.selectedToggleProperty().addListener(
				(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
					if (searchGroup.getSelectedToggle() != null && searchGroup.getSelectedToggle() instanceof RadioButton) {
						System.out.println("单选框事件监听，选中id=" + ((RadioButton) searchGroup.getSelectedToggle()).getId());
					}
				});
	}

	/**
	 * 搜索事件
	 */
	public void search() {
		SearchType searchType = null;
		if (searchGroup.getSelectedToggle() != null && searchGroup.getSelectedToggle() instanceof RadioButton) {
			searchType = SearchType.getSearchType(((RadioButton) searchGroup.getSelectedToggle()).getId());
		}
		boolean blurryIsSelected = blurryCheck.isSelected();
		String text = searchText.getText();
		System.out.println("searchType = " + searchType.name() + " ,blurryIsSelected =" + blurryIsSelected + " ,text = " + text);
	}

	/**
	 * 选择文件夹路径
	 */
	public String openDirectoryChooser(String title) {
		DirectoryChooser chooser = new DirectoryChooser();
		File defFile = new File(System.getProperty("user.dir"));
		if (defFile != null && defFile.isDirectory())
			chooser.setInitialDirectory(defFile);
		chooser.setTitle(title);
		File checkFile = chooser.showDialog(stage);
		if (checkFile != null) {
			final String path = checkFile.getAbsolutePath();
			return path;
		}
		return null;
	}

	/**
	 * btn 事件
	 */
	public void btnEvent(final ActionEvent event) {
		if (!(event.getSource() instanceof Button)) {
			FxUIUtil.openAlert(Alert.AlertType.ERROR, "控件类型错误", "错误提示");
			return;
		}
		Button btn = (Button) event.getSource();
		BtnEvent btnEvent = BtnEvent.getBtnEvent(btn.getId());
		String msg = null;
		if (btnEvent == null) {
			msg = "没有对应的事件处理器 id=" + btn.getId();
			FxUIUtil.openAlert(Alert.AlertType.ERROR, msg, "错误提示");
			FxUIUtil.sendMsgToTextInput(msgContent, msg, true);
			return;
		}
		switch (btnEvent) {
			case search: {
				search();
				break;
			}
			case svn_update: {
				SvnUtil.svnEvent(SvnUtil.SvnCommand.update, ConfigManager.getInstance().getLast_check_excel(), true, msgContent, "");
				break;
			}
			case svn_commit: {
				SvnUtil.svnEvent(SvnUtil.SvnCommand.commit, ConfigManager.getInstance().getLast_check_excel(), false, msgContent, "log");
				break;
			}
			case clean: {
				//TODO 清除操作
				break;
			}
			case remove: {
				//TODO 移除操作
				break;
			}
			case reloadCache: {
				//TODO 刷新缓存
				break;
			}
			case open: {
				//TODO 打开
				break;
			}
			case openOutPath: {
				final String path = openDirectoryChooser("选择输出路径");
				if (!StringUtil.isEmpty(path)) {
					if (!outPaths.getItems().contains(path)) {
						outPaths.getItems().add(path);
						outPaths.getSelectionModel().select(path);
					}
					ExecutorServiceUtil.getInstance().submit(() -> {
						ConfigManager.getInstance().writeOutPath(getChoiceSelect(excelPaths), path);
					});
				}

				break;
			}
			case openExcelPath: {
				final String path = openDirectoryChooser("选择excel路径");
				final boolean has = ConfigManager.getInstance().isHasPath(path);
				if (!StringUtil.isEmpty(path)) {
					if (!has) {
						excelPaths.getItems().add(path);
						excelPaths.getSelectionModel().select(path);
					}
					ExecutorServiceUtil.getInstance().submit(() -> {
						if (!has)
							ConfigManager.getInstance().write(ConfigManager.excel_path_array_key, path, true);
						ConfigManager.getInstance().write(ConfigManager.last_check_excel_key, path, false);
						loadTreeData(path);
					});
				}

				break;
			}
			case out_svn_commit: {
				SvnUtil.svnEvent(SvnUtil.SvnCommand.commit, getChoiceSelect(outPaths), false, msgContent, "log");
				break;
			}
			case out_svn_update: {
				SvnUtil.svnEvent(SvnUtil.SvnCommand.update, getChoiceSelect(outPaths), true, msgContent, "log");
				break;
			}
			case exportAll: {
				String excelPath = getChoiceSelect(excelPaths);
				String outPath = getChoiceSelect(outPaths);
				boolean json = checkJson.isSelected();
				boolean xml = checkXML.isSelected();
				boolean xd = checkXD.isSelected();
				//TODO 执行导出方法
				break;
			}
			case exportCheck: {
				String outPath = getChoiceSelect(outPaths);
				boolean json = checkJson.isSelected();
				boolean xml = checkXML.isSelected();
				boolean xd = checkXD.isSelected();
				//List<String> checkExcelPaths = getCheckExcelPaths();
				//TODO 执行导出方法
				break;
			}
			default: {
				FxUIUtil.openAlert(Alert.AlertType.ERROR, "未知操作类型! " + btnEvent.name(), "错误提示");
				break;
			}
		}
		if (!StringUtil.isEmpty(msg))
			FxUIUtil.sendMsgToTextInput(msgContent, msg, true);
	}

	public enum BtnEvent {
		svn_update("svn_update"),//svn 更新操作
		svn_commit("svn_commit"),//svn 提交操作
		remove("remove"),//移除
		reloadCache("reloadCache"),//刷新缓存
		open("open"),//打开
		clean("clean"),//清除
		openOutPath("openOutPath"),//打开输出路径
		openExcelPath("openExcelPath"),//选择excel路径
		search("search"),//搜索事件
		out_svn_update("out_svn_update"),//更新输出的svn
		out_svn_commit("out_svn_commit"),//提交输出的svn
		exportAll("exportAll"),//导出全部
		exportCheck("exportCheck"),//导出选中
		;
		private String id;

		BtnEvent(String id) {
			this.id = id;
		}

		private static Map<String, BtnEvent> cache;

		public static BtnEvent getBtnEvent(String id) {
			if (cache == null) {
				cache = new HashMap<>();
				for (BtnEvent svnEvent : values()) {
					cache.put(svnEvent.id, svnEvent);
				}
			}
			return cache.get(id);
		}

	}

	public enum SearchType {
		all("searchAll"),
		item("searchItem"),;
		private String id;

		SearchType(String id) {
			this.id = id;
		}

		private static Map<String, SearchType> cache;

		static {
			cache = new HashMap<>();
			for (SearchType searchType : values()) {
				cache.put(searchType.id, searchType);
			}
		}


		public static SearchType getSearchType(String id) {
			return cache.get(id);
		}

	}

}


