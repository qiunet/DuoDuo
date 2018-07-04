package org.qiunet.project.init.elements.info;

import org.qiunet.project.init.elements.entity.Entity;
import org.qiunet.template.parse.xml.SubVmElement;

import java.io.File;

/**
 * @author qiunet
 *         Created on 17/2/15 18:17.
 */
public class EntityInfo extends SubVmElement<EntityInfoVmElement> {
	private String poref;
	private String async;
	private String redisRef;
	private String dbInfoRef;

	public ElementRedisKey getRediskey(){
		return vmElement.getRedisKey();
	}
	public Bean getRedis(){
		return vmElement.getBean(redisRef);
	}
	public Bean getDbinfo(){
		return vmElement.getBean(dbInfoRef);
	}

	public String getRedisRef() {
		return redisRef;
	}
	public void setRedisRef(String redisRef) {
		this.redisRef = redisRef;
	}
	public String getDbInfoRef() {
		return dbInfoRef;
	}
	public void setDbInfoRef(String dbInfoRef) {
		this.dbInfoRef = dbInfoRef;
	}
	public String getAsync() {
		return async;
	}

	public void setAsync(String async) {
		this.async = async;
	}

	public String getVo() {
		return getEntityAliasName()+"Vo";
	}

	public String getPoref() {
		return poref;
	}

	public void setPoref(String poref) {
		this.poref = poref;
	}

	private String getEntityAliasName(){
		return poref.substring(0, poref.length() - 2);
	}
	/**
	 * 数据库mybatis 的nameSpace
	 * @return
	 */
	public String getNameSpace(){
		if (poref.endsWith("Po")) return poref.substring(0, poref.length() - 2).toLowerCase();
		return poref.toLowerCase();
	}
	@Override
	protected String getOutFilePath() {
		if (getEntity(poref) == null) {
			throw new RuntimeException("poref ["+poref+"] is not in "+getProjectConfig().getEntityXmlPath());
		}
		return getInfoPackagePath().replace(".", File.separator);
	}

	/***
	 * 得到infoPackagePath
	 * @return
	 */
	public String getInfoPackagePath(){
		Entity entity = getEntity(poref);
		return entity.getPackagePath().substring(0, entity.getPackagePath().lastIndexOf('.'))+".info";
	}

	/***
	 * 得到infoPackagePath
	 * @return
	 */
	public String getServicePackagePath(){
		Entity entity = getEntity(poref);
		return entity.getPackagePath().substring(0, entity.getPackagePath().lastIndexOf('.'))+".service";
	}

	public String getServiceFileName(){
		return getEntityAliasName()+"Service";
	}
}
