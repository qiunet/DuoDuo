package org.qiunet.project.init.elements.entity;

import org.qiunet.utils.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiunet.
 * 17/7/11
 */
public class Constructor {

	private List<Field> fields = new ArrayList<>();

	private String constructorFields;

	/**
	 * 把字符串序列成field对象
	 * @param fields
	 */
	public void refreshFields(List<Field> fields){
		String cFields [] = StringUtil.split(constructorFields, ",");
		for (String field : cFields) {
			field = field.trim();
			Field target = null;
			for (Field f : fields) {
				if (f.getName().equals(field)) {
					target = f;
					break;
				}
			}
			if (target == null) {
				throw new NullPointerException("constructor field ["+field+"] is invalid!");
			}
			this.fields.add(target);
		}
	}

	public List<Field> getFields() {
		return fields;
	}

	public String getConstructorFields() {
		return constructorFields;
	}

	public void setConstructorFields(String constructorFields) {
		this.constructorFields = constructorFields;
	}
}
