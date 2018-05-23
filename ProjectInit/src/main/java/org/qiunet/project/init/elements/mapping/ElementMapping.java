package org.qiunet.project.init.elements.mapping;

import org.qiunet.project.init.IProjectInitConfig;
import org.qiunet.project.init.elements.entity.Entity;
import org.qiunet.project.init.elements.entity.Field;
import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.template.parse.xml.VmElement;
import org.qiunet.utils.string.StringUtil;

/**
 * @author qiunet
 *         Created on 17/2/22 08:13.
 */
public class ElementMapping extends SubVmElement {
	private String poref;
	private boolean uniqid;
	private String selectKey;
	private String tablePrefix;
	private boolean splitTable;

	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	public String getSelectKey() {
		return selectKey;
	}

	public void setSelectKey(String selectKey) {
		this.selectKey = selectKey;
	}

	public boolean isUniqid() {
		return uniqid;
	}

	public void setUniqid(boolean uniqid) {
		this.uniqid = uniqid;
	}

	public boolean isSplitTable() {
		return splitTable;
	}

	public void setSplitTable(boolean splitTable) {
		this.splitTable = splitTable;
	}

	public String getPoref() {
		return poref;
	}

	public String getTableName(){
		Entity entity = ((VmElement<Entity>)base.getParam("entity")).subVmElement(poref);
		if (entity == null) {
			throw new RuntimeException("poref ["+poref+"] is not in "+ ((IProjectInitConfig) base.getParam("baseConfig")).getEntityXmlPath());
		}

		StringBuilder sb = new StringBuilder();
		sb.append("${dbName}.");

		if (!StringUtil.isEmpty(tablePrefix)) {
			sb.append(tablePrefix);
		} else {
			sb.append(entity.getAliasName());
		}

		if (entity.getType().isPlatformType()) {
			sb.append("_${platformName}");
		}
		if(entity.getType().isListType() && isSplitTable()) {
			sb.append("_${tbIndex}");
		}
		return sb.toString();
	}

	/**
	 * 根据各种情况拼的sql语句
	 * @return
	 */
	public String getSelectSql(){
		Entity entity = ((VmElement<Entity>)base.getParam("entity")).subVmElement(poref);
		if (entity == null) {
			throw new RuntimeException("poref ["+poref+"] is not in "+ ((IProjectInitConfig) base.getParam("baseConfig")).getEntityXmlPath());
		}

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		for (Field field : entity.getFields()) {
			sb.append('`').append(field.getName()).append("`,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" from ").append(getTableName());
		sb.append(" where ").append(entity.getDbInfoKey()).append('=').append("#{").append(entity.getDbInfoKey()).append("};");
		return sb.toString();
	}

	/**
	 * 根据各种情况拼的sql语句
	 * @return
	 */
	public String getUpdateSql(){
		Entity entity = ((VmElement<Entity>)base.getParam("entity")).subVmElement(poref);
		if (entity == null) {
			throw new RuntimeException("poref ["+poref+"] is not in  "+ ((IProjectInitConfig) base.getParam("baseConfig")).getEntityXmlPath());
		}

		StringBuilder sb = new StringBuilder();
		sb.append("update ").append(getTableName()).append(" set ");
		for (Field field : entity.getFields()) {
			if (field.getName().equals(entity.getDbInfoKey()) || field.getName().equals(entity.getSubKey())) continue;

			sb.append('`').append(field.getName()).append("` = #{").append(field.getName()).append("} ,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" where ");
		if (isUniqid() && entity.getType().isListType()) {
			// 是list 类型. 并且需要提前
			sb.append('`').append(entity.getSubKey()).append("` = #{subId}");
		}else {
			sb.append('`').append(entity.getDbInfoKey()).append("` = #{").append(entity.getDbInfoKey()).append("}");
			if (entity.getType().isListType()) {
				sb.append(" and `").append(entity.getSubKey()).append("` = #{subId}");
			}
		}
		sb.append(";");
		return sb.toString();
	}
	/**
	 * 根据各种情况拼的sql语句
	 * @return
	 */
	public String getInsertSql(){
		Entity entity = ((VmElement<Entity>)base.getParam("entity")).subVmElement(poref);
		if (entity == null) {
			throw new RuntimeException("poref ["+poref+"] is not in "+ ((IProjectInitConfig) base.getParam("baseConfig")).getEntityXmlPath());
		}

		StringBuilder sb = new StringBuilder();
		sb.append("insert into ").append(getTableName()).append(" (");
		for (Field field : entity.getFields()) {
			sb.append('`').append(field.getName()).append("`,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") values (");
		for (Field field : entity.getFields()) {
			sb.append("#{").append(field.getName()).append("} ,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(");");
		if (!StringUtil.isEmpty(selectKey)) {
			Field field = null;
			for (Field f : entity.getFields()) {
				if (f.getName().equals(selectKey)) {
					field = f;
					break;
				}
			}
			sb.append("\n\t\t<selectKey resultType=\"").append(field.getType()).append("\" keyProperty=\"").append(selectKey).append("\">\n");
			sb.append("\t\t\tSELECT @@IDENTITY AS ").append(selectKey).append(";\n");
			sb.append("\t\t</selectKey>");
		}
		return sb.toString();
	}/**
	 * 根据各种情况拼的sql语句
	 * @return
	 */
	public String getDeleteSql(){
		Entity entity = ((VmElement<Entity>)base.getParam("entity")).subVmElement(poref);
		if (entity == null) {
			throw new RuntimeException("poref ["+poref+"] is not in  "+ ((IProjectInitConfig) base.getParam("baseConfig")).getEntityXmlPath());
		}

		StringBuilder sb = new StringBuilder();
		sb.append("delete from ").append(getTableName()).append(" where ");
		if (isUniqid() && entity.getType().isListType()) {
			// 是list 类型. 并且需要提前
			sb.append('`').append(entity.getSubKey()).append("` = #{subId}");
		}else {
			sb.append('`').append(entity.getDbInfoKey()).append("` = #{").append(entity.getDbInfoKey()).append("}");
			if (entity.getType().isListType()) {
				sb.append("  and `").append(entity.getSubKey()).append("` = #{subId}");
			}
		}
		sb.append(";");
		return sb.toString();
	}
	public String getSelectStatment(){
		Entity entity = ((VmElement<Entity>)base.getParam("entity")).subVmElement(poref);
		if (entity == null) {
			throw new RuntimeException("poref ["+poref+"] is not in "+ ((IProjectInitConfig) base.getParam("baseConfig")).getEntityXmlPath());
		}

		String selectStatment = "get"+poref;
		if (entity.getType().isListType()) {
			selectStatment += "s";
		}
		return  selectStatment;
	}

	public String getInsertStatment(){
		return  "insert"+poref;
	}
	public String getDeleteStatment(){
		return  "delete"+poref;
	}
	public String getUpdateStatment(){
		return  "update"+poref;
	}
	public void setPoref(String poref) {
		this.poref = poref;
	}

	public String getNameSpace(){
		if (poref.endsWith("Po")) return poref.substring(0, poref.length() - 2).toLowerCase();
		return poref.toLowerCase();
	}
	@Override
	protected String getOutFilePath() {
		return "";
	}
}
