package org.qiunet.fx.bean;

import javafx.beans.property.*;

import java.io.File;

public class TableData {
	private final StringProperty name = new SimpleStringProperty();
	private final BooleanProperty check = new SimpleBooleanProperty();

	public StringProperty nameProperty() {
		return name;
	}


	public BooleanProperty checkProperty() {
		return check;
	}

	public TableData(File file) {
		setName(file.getAbsolutePath());
		setCheck(true);
	}

	public TableData() {

	}


	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		nameProperty().set(name);
	}


	public boolean isCheck() {
		return check.get();
	}

	public void setCheck(boolean check) {
		checkProperty().set(check);
	}

}
