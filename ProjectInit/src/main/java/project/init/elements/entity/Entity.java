package project.init.elements.entity;

import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.template.parse.xml.VmElement;
import project.init.elements.mapping.ElementMapping;
import project.init.enums.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiunet
 *         Created on 16/11/21 13:15.
 */
public class Entity extends SubVmElement {

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
	public boolean isRedisList(){
		return entityType == EntityType.RedisList || entityType == EntityType.PlatformRedisList;
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

	public String getAliasName(){
		if (getName().endsWith("Po")) return getName().substring(0, getName().length() - 2).toLowerCase();
		return getName().toLowerCase();
	}

	public String getConfigFileName(String poName){
		VmElement<ElementMapping> vmElement = ((VmElement<ElementMapping>)base.getParam("mapping"));
		for (ElementMapping sub : vmElement.getSubVmElementList()) {
			if (sub.getPoref().equals(poName)) return sub.getName();
		}
		return "";
	}

}
