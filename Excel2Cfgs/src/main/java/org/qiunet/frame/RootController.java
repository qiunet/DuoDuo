package org.qiunet.frame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.qiunet.frame.component.CfgTreeCell;
import org.qiunet.frame.enums.ActionType;
import org.qiunet.frame.component.FileTreeItem;
import org.qiunet.frame.enums.RoleType;
import org.qiunet.frame.setting.SettingManager;
import org.qiunet.utils.string.StringUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.qiunet.frame.enums.RoleType.*;

/***
 *
 *
 * @author qiunet
 * 2019-11-05 18:13
 ***/
public class RootController {
	private static RootController instance;

	private Stage primaryStage;
	@FXML
	public TreeView<File> excelNames;

	@FXML
	public CheckBox xdBox;
	@FXML
	public CheckBox jsonBox;
	@FXML
	public CheckBox xmlBox;

	private boolean treeViewInited;
	@FXML
	public TextArea console;

	@FXML
	public ChoiceBox<String> excelPaths;

	@FXML
	public ChoiceBox<RoleType> roleType;

	@FXML
	public ChoiceBox<String> cfgPaths;

	@FXML
	private HBox projectInfo;

	public static RootController getInstance() {
		return instance;
	}

	public RootController() {
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
		}
		instance = this;
	}

	public void init(Stage stage) {
		this.initExcelTreeView();
		this.initExcelChoiceBox();
		this.primaryStage = stage;
		this.initCfgChoiceBox();
		this.initRoleType();
		this.initCheckBox();
	}

	/***
	 * 初始化输出模式
	 */
	private void initRoleType() {
		this.roleType.getItems().addAll(RoleType.values());
		RoleType currRoleType = SettingManager.getInstance().getSetting().getRoleType();
		this.roleType.getSelectionModel().selectedItemProperty().addListener((v1, o1, n2) -> {
				SettingManager.getInstance().getSetting().setRoleType(n2);
				projectInfo.setDisable(n2 == SCHEMER);
			}
		);
		this.roleType.getSelectionModel().select(currRoleType);
	}
	/***
	 * 初始化左边文件树
	 */
	private void initExcelTreeView(){
		this.excelPaths.getSelectionModel().selectedItemProperty().addListener((v1, o1, n2) -> this.refreshExcelTree());
		this.excelNames.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
				File file = excelNames.getSelectionModel().getSelectedItem().getValue();
				if (file.isDirectory()) {
					return;
				}
				try {
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/***
	 * 初始化输出配置文件路径 下拉选择框
	 */
	private void initCfgChoiceBox() {
		List<String> cfgPathStrs = SettingManager.getInstance().getCfgPaths();
		if (cfgPathStrs == null || cfgPathStrs.isEmpty()) {
			return;
		}

		this.cfgPaths.getItems().addAll(cfgPathStrs);
		this.cfgPaths.getSelectionModel().select(cfgPathStrs.get(0));
	}
	/***
	 * 初始化Excel路径 下拉选择框
	 */
	private void initExcelChoiceBox() {
		List<String> excelPathStrs = SettingManager.getInstance().getExcelPaths();
		if (excelPathStrs == null || excelPathStrs.isEmpty()) {
			return;
		}

		this.excelPaths.getItems().addAll(excelPathStrs);
		this.excelPaths.getSelectionModel().select(excelPathStrs.get(0));
	}

	/***
	 * 初始化 输出格式 多选框.
	 */
	private void initCheckBox(){
		this.jsonBox.setSelected(SettingManager.getInstance().getSetting().isJsonChecked());
		this.xmlBox.setSelected(SettingManager.getInstance().getSetting().isXmlChecked());
		this.xdBox.setSelected(SettingManager.getInstance().getSetting().isXdChecked());
	}

	/***
	 * 按钮的事件处理
	 * @param event
	 */
	public void componentAction(ActionEvent event) {
		ActionType type = ActionType.valueOf(((Control) event.getSource()).getId());
		type.handlerAction(primaryStage, event, this);
	}

	/**
	 * 刷新excel列表
	 */
	public void refreshExcelTree(){
		String path = this.excelPaths.getSelectionModel().getSelectedItem();
		if (StringUtil.isEmpty(path)) {
			excelNames.setRoot(null);
			return;
		}

		boolean treeViewInited = this.treeViewInited;
		this.treeViewInited = true;

		File rootFile = new File(path);
		excelNames.setRoot(new FileTreeItem(rootFile));

		if (! treeViewInited) {
			excelNames.setShowRoot(true);
			excelNames.setCellFactory(item -> new CfgTreeCell(this.console));
		}
	}
}
