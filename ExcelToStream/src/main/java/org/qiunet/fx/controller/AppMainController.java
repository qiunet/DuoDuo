package org.qiunet.fx.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.qiunet.fx.common.ConfigManager;
import org.qiunet.utils.ExecutorServiceUtil;
import org.qiunet.utils.string.StringUtil;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * created by wgw on 2019/7/28
 */
public class AppMainController extends BaseController {

	private Stage stage;
	@FXML
	private TabPane tabPane;

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

	@Override
	public void init(Object... objs) {
		if (objs.length > 0 && objs[0] instanceof Stage)
			this.stage = (Stage) objs[0];
		/**初始化单选框*/
		initToggleGroup();
		/**初始化下拉选择框*/
		intiExcelPaths();
	}

	public void intiExcelPaths() {
		Set<String> set = ConfigManager.getInstance().getExcel_path_array();
		if (set != null && !set.isEmpty())
			excelPaths.getItems().addAll(set);
		String last_excel = ConfigManager.getInstance().getLast_check_excel();
		if (!StringUtil.isEmpty(last_excel))
			excelPaths.getSelectionModel().select(last_excel);
		//下拉选择框事件监听
		excelPaths.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
			String path = excelPaths.getItems().get(excelPaths.getSelectionModel().getSelectedIndex());
			ConfigManager.getInstance().write(ConfigManager.last_check_excel_key, path, false);
		}));
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
	public void searchBtn() {
		SearchType searchType = null;
		if (searchGroup.getSelectedToggle() != null && searchGroup.getSelectedToggle() instanceof RadioButton) {
			searchType = SearchType.getSearchType(((RadioButton) searchGroup.getSelectedToggle()).getId());
		}
		boolean blurryIsSelected = blurryCheck.isSelected();
		String text = searchText.getText();
		System.out.println("searchType = " + searchType.name() + " ,blurryIsSelected =" + blurryIsSelected + " ,text = " + text);
	}

	/**
	 * 选择excel路径
	 */
	public void openExcelFile() {
		DirectoryChooser chooser = new DirectoryChooser();
		File defFile = new File(System.getProperty("user.dir"));
		if (defFile != null && defFile.isDirectory())
			chooser.setInitialDirectory(defFile);
		chooser.setTitle("选择excel路径");
		File checkFile = chooser.showDialog(stage);
		if (checkFile != null) {
			final String path = checkFile.getAbsolutePath();
			excelPaths.getItems().add(path);
			excelPaths.getSelectionModel().select(path);
			ExecutorServiceUtil.getInstance().submit(() -> {
				ConfigManager.getInstance().write(ConfigManager.excel_path_array_key, path, true);
				ConfigManager.getInstance().write(ConfigManager.last_check_excel_key, path, false);
			});
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


