package project.init.elements.info;

import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.template.parse.xml.VmElement;
import project.init.elements.entity.Entity;
import project.init.xmlparse.BeanVmElement;

import java.io.File;

/**
 * @author qiunet
 *         Created on 17/2/15 18:17.
 */
public class EntityInfo extends SubVmElement {
	private String poref;
	private String vo;
	private String async;
	private String redisRef;
	private String dbInfoRef;

	public ElementRedisKey getRediskey(){
		return ((BeanVmElement) base).getRedisKey();
	}
	public Bean getRedis(){
		return ((BeanVmElement)base).getBean(redisRef);
	}
	public Bean getDbinfo(){
		return ((BeanVmElement)base).getBean(dbInfoRef);
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
		return vo;
	}

	public void setVo(String vo) {
		this.vo = vo;
	}

	public String getPoref() {
		return poref;
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
		if (base.getParam("entity") == null
		|| ((VmElement)base.getParam("entity")).subVmElement(poref) == null) {
			throw new RuntimeException("poref ["+poref+"] is not in entity_create.xml");
		}
		return ((Entity)((VmElement)base.getParam("entity")).subVmElement(poref)).getInfoPackagePath().replace(".", File.separator);
	}
}
