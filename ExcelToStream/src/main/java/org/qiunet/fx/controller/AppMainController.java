package org.qiunet.fx.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.qiunet.fx.bean.TableData;
import org.qiunet.fx.common.ConfigManager;
import org.qiunet.utils.ExecutorServiceUtil;
import org.qiunet.utils.FileUtil;
import org.qiunet.utils.FxUIUtil;
import org.qiunet.utils.SvnUtil;
import org.qiunet.utils.string.StringUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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
	//excel列表的缓存
	private Map<String, ObservableList<TableData>> tabDataCache = new HashMap<>();

	//信息输出
	@FXML
	private TextArea msgContent;
	@FXML
	private ChoiceBox<String> outPaths;

	/**
	 * 表格数据
	 */
	@FXML
	private TableView<TableData> table;
	@FXML
	private TableColumn<TableData, String> tab_name;
	@FXML
	private TableColumn<TableData, Boolean> tab_check;

	@FXML
	private CheckBox checkJson;
	@FXML
	private CheckBox checkXML;
	@FXML
	private CheckBox checkXD;
	@FXML
	private CheckBox checkAll;
	@FXML
	private Label allCount;
	@FXML
	private Label checkCount;

	@Override
	public void init(Object... objs) {
		if (objs.length > 0 && objs[0] instanceof Stage)
			this.stage = (Stage) objs[0];
		/**初始化单选框*/
		initToggleGroup();
		/**初始化下拉选择框*/
		intiChoiceBox();

		checkBoxListener();
	}

	public void checkBoxListener() {
		checkAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
			table.getItems().stream().forEach(tableData -> tableData.setCheck(newValue));
			setCount();
		});

	}

	public void setCount() {
		allCount.setText(table.getItems().size()+"");
		checkCount.setText(getCheckExcelPaths().size()+"");
	}

	//加载表格数据
	public ObservableList<TableData> loadTableData(String filePath) {
		if (StringUtil.isEmpty(filePath))
			return FXCollections.observableArrayList();
		ObservableList<TableData> list = tabDataCache.get(filePath);
		if (list == null) {
			list = FXCollections.observableArrayList();
			List<File> pathList = FileUtil.getExcelArrays(filePath);

			if (!pathList.isEmpty()) {
				for (File file : pathList) {
					list.add(new TableData(file));
				}
			}
			tabDataCache.put(filePath, list);
		}
		return tabDataCache.get(filePath);
	}

	//显示列表
	public void showTableData(final ObservableList<TableData> list) {
		FxUIUtil.addUITask(() -> {
			tab_name.setCellValueFactory(cell -> cell.getValue().nameProperty());
			tab_check.setCellValueFactory(cell -> cell.getValue().checkProperty());
			tab_check.setCellFactory((col) -> {
				TableCell<TableData, Boolean> cell = new TableCell<TableData, Boolean>() {
					@Override
					public void updateItem(Boolean item, boolean empty) {
						super.updateItem(item, empty);
						this.setText(null);
						this.setGraphic(null);
						if (!empty) {
							CheckBox checkBox = new CheckBox();
							if (item != null)
								checkBox.setSelected(item);
							this.setGraphic(checkBox);
							checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
								TableData tableData = this.getTableView().getItems().get(getIndex());
								tableData.setCheck(checkBox.isSelected());
								setCount();
							});
						}
					}
				};
				return cell;
			});
			table.setItems(list);
			setCount();
		});
	}

	/**
	 * 获取选中的列表
	 *
	 * @return
	 */
	public List<String> getCheckExcelPaths() {
		ObservableList<TableData> list = table.getItems();
		if (list == null || list.isEmpty())
			return Collections.emptyList();
		return list.stream().filter(data -> data.isCheck()).map(data -> data.getName()).collect(Collectors.toList());
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
			showTableData(loadTableData(last_excel));
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
			showTableData(loadTableData(path));
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
				msg = SvnUtil.svnEvent(SvnUtil.SvnCommand.update, ConfigManager.getInstance().getLast_check_excel(), true);
				break;
			}
			case svn_commit: {
				msg = SvnUtil.svnEvent(SvnUtil.SvnCommand.commit, ConfigManager.getInstance().getLast_check_excel(), false);
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
						showTableData(loadTableData(path));
					});
				}

				break;
			}
			case out_svn_commit: {
				msg = SvnUtil.svnEvent(SvnUtil.SvnCommand.commit, getChoiceSelect(outPaths), false);
				break;
			}
			case out_svn_update: {
				msg = SvnUtil.svnEvent(SvnUtil.SvnCommand.update, getChoiceSelect(outPaths), true);
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
				List<String> checkExcelPaths = getCheckExcelPaths();
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


