package org.qiunet.template.parse.xml;

import org.apache.commons.collections.map.HashedMap;
import org.qiunet.utils.string.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  vm 解析的上层, 可以包含若干一样的subvmelement元素,
 *  如果xml本身 没有vmfilePath 和  dirName  filePostfix 则可以自己构建类继承该类, 直接指定.
 * @author qiunet
 *         Created on 16/11/21 07:59.
 */
public class VmElement<T extends SubVmElement> {
	private String baseDir;
	private String vmfilePath;
	private String filePostfix;
	private Map<String, Object> params;
	private List<T> subVmElements = new ArrayList();
	private Map<String, T> subVmElementMap = new HashedMap();
	public String getBaseDir() {
		return baseDir;
	}
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	public void setVmfilePath(String vmfilePath) {
		this.vmfilePath = vmfilePath;
	}

	public void setFilePostfix(String filePostfix) {
		this.filePostfix = filePostfix;
	}

	public String getVmfilePath() {
		return vmfilePath;
	}

	public String getFilePostfix() {
		return filePostfix;
	}
	/**
	 * 初始化一些参数
	 * @param params
	 */
	public void initParams(Map<String, Object> params) {
		this.params = params;
	}
	public void addSubElement(T element) {
		if (StringUtil.isEmpty(element.getName())){
			throw new NullPointerException("element ["+element.getClass().getName()+"]name is empty! ");
		}
		if (! subVmElements.isEmpty()){
			String nowClassName = subVmElements.get(0).getClass().getName();
			String argClassName = element.getClass().getName();
			if(!nowClassName.equals(argClassName))
				throw new IllegalArgumentException("VmElement Child must be same! ["+nowClassName+"] not equals ["+argClassName+"]");
		}
		element.setBase(this);
		this.subVmElements.add(element);
		this.subVmElementMap.put(element.getName(), element);
	}
	/**
	 * 根据名称得到一个 SubVmElement 
	 * @param name
	 * @return
	 */
	public T subVmElement(String name){
		if (! subVmElementMap.containsKey(name)) {
			throw new NullPointerException("name ["+name+"] is not in Map");
		}
		return subVmElementMap.get(name);
	}
	/***
	 * 得到subElements的list
	 * @return
	 */
	public List<T> getSubVmElementList(){
		return subVmElements;
	}
	
	public void parseVm(String baseDir) {
		if(! baseDir.endsWith(File.separator)) baseDir += File.separator;
		baseDir += this.baseDir;
		
		if(! baseDir.endsWith(File.separator)) baseDir += File.separator;
		
		for(SubVmElement sub : subVmElements) {
			sub.parseOutFile(baseDir + sub.getOutFilePath(), getVmfilePath(), getFilePostfix());
		}
	}
	
	public Object getParam(String key){
		return params.get(key);
	}
}
