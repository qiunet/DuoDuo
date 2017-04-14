package elements.entity;

import enums.EntityType;
import org.qiunet.template.parse.xml.SubVmElement;

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
		this.subKey = subKey;
	}
	public void setEntityType(String entityType) {
		this.entityType = EntityType.parse(entityType);
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
		return getName().replaceAll("Po", "").toLowerCase();
	}
}
