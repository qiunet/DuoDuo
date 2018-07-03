package org.qiunet.project.init.elements.entity;

import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.project.init.enums.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiunet
 *         Created on 16/11/21 13:15.
 */
public class Entity extends SubVmElement<EntityVmElement> {

	private String dbInfoKey;
	private String subKey;
	private String packagePath;
	private EntityType entityType;
	private List<Field> fields = new ArrayList<>();
	private List<Constructor> constructors = new ArrayList<>();


	public String getInfoPackagePath(){
		return packagePath.substring(0, packagePath.lastIndexOf('.'))+".info";
	}
	public String getPackagePath() {
		return packagePath;
	}
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	public void addField(Field field){
		this.fields.add(field);
	}
	public void addConstructor (Constructor constructor) {
		this.constructors.add(constructor);
	}

	public List<Constructor> getConstructors() {
		for (Constructor constructor : constructors) {
			constructor.refreshFields(fields);
		}
		return constructors;
	}

	public List<Field> getFields() {
		return fields;
	}
	public String getDbInfoKey() {
		return dbInfoKey;
	}
	public void setDbInfoKey(String dbInfoKey) {
		this.dbInfoKey = dbInfoKey;
	}

	public String getSubKey() {
		return subKey;
	}

	/***
	 * 得到SubKey 类型
	 * @return
	 */
	public String getSubKeyType(){
		return getKeyType(this.subKey);
	}

	/***
	 * 得到 DbInfoKey 类型
	 * @return
	 */
	public String getDbInfoKeyType() {
		return getKeyType(this.dbInfoKey);
	}

	/***
	 * 得到 dbINfoKey 或者 subKey 的类型. 用来作为泛型使用
	 * @param fieldName
	 * @return
	 */
	private String getKeyType(String fieldName){
		Field field = null;
		for (Field f : fields) {
			if (f.getName().equals(fieldName)) {
				field = f;
				break;
			}
		}

		if (field == null) {
			throw new NullPointerException("poName ["+this.getName()+"] not have key field ["+fieldName+"]");
		}

		switch (field.getType()) {
			case "int":
				return "Integer";
			case "String":
				return "String";
			default:
				throw new NullPointerException("poName ["+this.getName()+"] key ["+fieldName+"] type not support. just string and number");
		}

	}


	public void setSubKey(String subKey) {
		if (entityType == null || isRedisList()) {
			this.subKey = subKey;
		}
	}

	public void setEntityType(String entityType) {
		this.entityType = EntityType.parse(entityType);
		if (! isRedisList()) this.subKey = null;
	}
	public String getEntityType() {
		return entityType.toString();
	}

	public String getClassInfo(){
		return entityType.getClazz().getName();
	}

	/***
	 * 是否是redisList
	 * @return
	 */
	public boolean isRedisList(){
		return entityType == EntityType.RedisList || entityType == EntityType.PlatformRedisList;
	}

	/***
	 * 是否是 platform 对象
	 * @return
	 */
	public boolean isPlatformObj(){
		return entityType == EntityType.PlatformRedisList || entityType == EntityType.PlatformRedisEntity;
	}

	@Override
	public String getOutFilePath() {
		String path = packagePath.replace(".", File.separator);
		if (!path.endsWith(File.separator)) path += File.separator;
		return path;
	}

	public EntityType getType(){
		return entityType;
	}

//	public String getConfigFileName(String poName){
//		VmElement<ElementMapping> vmElement = ((VmElement<ElementMapping>)base.getParam("mapping"));
//		for (ElementMapping sub : vmElement.getSubVmElementList()) {
//			if (sub.getPoref().equals(poName)) return sub.getName();
//		}
//
//		IProjectInitConfig config = (IProjectInitConfig) base.getParam("baseConfig");
//		throw new NullPointerException("poName ["+poName+"] is not set in ["+config.getMabatisMappingXmlPath()+"]");
//	}

}
