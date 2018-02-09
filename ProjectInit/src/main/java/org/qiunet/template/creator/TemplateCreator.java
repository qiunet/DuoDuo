package org.qiunet.template.creator;

import org.qiunet.template.config.Constants;
import org.qiunet.template.parse.template.VelocityFactory;
import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.template.parse.xml.VmElement;

import java.util.Map;

/**
 * @author qiunet
 *         Created on 16/11/16 20:39.
 */
public class TemplateCreator<T extends SubVmElement> {
	private BaseXmlParse parse;
	/**循环该 xml得到SubVmElement 的 对象名称*/
	private String dataObjName;
	/**其他需要添加到velocity的参数对象. 名称自己指定. 不能包含rootObjName*/
	private Map<String, Object> params;
	
	private VmElement<T> vmBase;

	public void addVmBase(VmElement<T> vmBase){
		this.vmBase = vmBase;
	}

	public TemplateCreator(BaseXmlParse parse) throws Exception {
		this(parse, Constants.DEFAULT_DATA_OBJECT_NAME);
	}
	public TemplateCreator(BaseXmlParse parse, Map<String ,Object> params) throws Exception {
		this(parse, Constants.DEFAULT_DATA_OBJECT_NAME,params);
	}
	public TemplateCreator(BaseXmlParse parse, String dataObjName) throws Exception {
		this(parse, dataObjName,null);
	}
	public TemplateCreator(BaseXmlParse parse, String dataObjName, Map<String ,Object> params) throws Exception {
		this.parse = parse;
		this.params = params;
		this.dataObjName = dataObjName;
		
		if (params != null && params.containsKey(dataObjName)) throw new Exception("can container rootObjName");
	}

	/***
	 * 输出base
	 */
	public VmElement<T> parseTemplate() {
		parse.setValidating(false);
		parse.push(this ,"addVmBase");
		parse.parseXml();
		parse.parse();

		this.vmBase.initParams(params);
		// 输出basevm信息
		VelocityFactory.getInstance().initVelocityEngine(dataObjName, params);
		vmBase.parseVm(parse.getBasePath());
		return vmBase;
	}
}
