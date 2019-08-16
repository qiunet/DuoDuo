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
	/***
	 * 对象的类名
	 */
	private String name;
	/***
	 * 主键
	 */
	private String key;
	/***
	 * 包名相对于userDir的路径
	 */
	private String baseDir;
	/***
	 * 包名 路径
	 */
	private String packageName;
	/***
	 * 所有的字段定义
	 */
	private List<FieldDefine> fieldDefines = new ArrayList<>();
	/***
	 * 所有的构造函数定义
	 */
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

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
