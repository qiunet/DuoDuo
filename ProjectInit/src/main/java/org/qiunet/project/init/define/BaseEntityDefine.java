package org.qiunet.project.init.define;

import java.util.ArrayList;
import java.util.List;

/***
 *
 *
 * qiunet
 * 2019-08-14 22:31
 ***/
public class BaseEntityDefine implements IEntityDefine {
	private String name;

	private String key;

	private List<FieldDefine> fieldDefines = new ArrayList<>();

	private List<ConstructorDefine> constructorDefines = new ArrayList<>();
	@Override
	public String getDoName() {
		return name;
	}

	@Override
	public String getBoName() {
		return name.replace("Do", "Bo");
	}

	@Override
	public List<ConstructorDefine> getConstructorDefines() {
		return constructorDefines;
	}

	@Override
	public List<FieldDefine> getFieldDefines() {
		return fieldDefines;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void addField(FieldDefine fieldDefine) {
		this.fieldDefines.add(fieldDefine);
	}

	public void addConstructor(ConstructorDefine constructorDefine){
		this.constructorDefines.add(constructorDefine);
		constructorDefine.init(this);
	}
}
