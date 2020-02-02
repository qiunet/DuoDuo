package org.qiunet.excel2cfgs.frame.component;

import javafx.util.StringConverter;

import java.io.File;

/***
 *
 *
 * @author qiunet
 * 2020-01-19 17:01
 ***/
public class FileStringConvert extends StringConverter<File> {
	@Override
	public String toString(File object) {
		return object.getName();
	}

	@Override
	public File fromString(String string) {
		return new File(string);
	}
}
