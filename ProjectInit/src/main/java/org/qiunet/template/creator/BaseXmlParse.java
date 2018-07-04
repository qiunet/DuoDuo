package org.qiunet.template.creator;

import org.apache.commons.digester.Digester;
import org.qiunet.template.parse.xml.VmElement;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * 初始解析xml使用的
 * @author qiunet
 *         Created on 16/11/18 17:40.
 */
public abstract class BaseXmlParse<vmelement extends VmElement> {
	private Class<vmelement> vmElementClass;

	private Digester digester;

	private String xmlFile;

	/**
	 * 可以自己传一个自己定义的vmelement
	 * @param vmElementClass vmelement 的class
	 * @param xmlConfigPath xml路径
	 */
	public BaseXmlParse(Class<vmelement> vmElementClass, String xmlConfigPath){
		this.xmlFile = xmlConfigPath;
		this.digester = new Digester();
		this.vmElementClass = vmElementClass;
	}

	public void setValidating(boolean validate){
		digester.setValidating(validate);
	}

	void push(Object root, String addMethod) {
		this.digester.push(root);

		this.addObjectCreate("base", vmElementClass ,  addMethod);
	}

	/**
	 * xml 最终生成对象.
	 */
	void parse(){
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFile);
			this.digester.parse(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 继承类 处理自己的xml解析.
	 */
	public abstract void parseXml();

	/**
	 * 对digester处理
	 * @param pattern xml 路径内容
	 * @param clazz 对应的class
	 * @param setNext 往上层路径传入的方法
	 */
	protected void addObjectCreate(String pattern, Class<?> clazz, String setNext) {
		digester.addObjectCreate(pattern, clazz);
		digester.addSetProperties(pattern);
		digester.addSetNext(pattern,setNext);
	}

	/***
	 * 使用默认的方法 (addSubElement) 创建一个ObjectCreate
	 * @param pattern xml 路径内容
	 * @param clazz 对应的class
	 */
	protected  void addObjectCreate(String pattern, Class<?> clazz){
		this.addObjectCreate(pattern, clazz, "addSubElement");
	}
}
