package org.qiunet.project.init.enums;

import org.apache.commons.digester.Digester;
import org.qiunet.project.init.define.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/****
 * Entity 的类型. 目前支持3个类型.
 * 分entity entity_list 共计6个
 *
 * qiunet
 * 2019-08-14 16:37
 */
public enum  EntityType {

	DB_ENTITY("DbEntity.xsd") {
		@Override
		public void initDigester(Digester digester) {
			this.addObjectCreate(digester, "db_entity", DbEntityDefine.class, "setEntityDefine");
			this.addObjectCreate(digester, "db_entity/field", FieldDefine.class, "addField");
			this.addObjectCreate(digester, "db_entity/constructor", ConstructorDefine.class, "addConstructor");
			this.addObjectCreate(digester, "db_entity/constructor/constructor_arg", ConstructorArgDefine.class, "addField");
		}
	},

	CACHE_ENTITY("CacheEntity.xsd") {
		@Override
		public void initDigester(Digester digester) {
			this.addObjectCreate(digester, "cache_entity", CacheEntityDefine.class, "setEntityDefine");
			this.addObjectCreate(digester, "cache_entity/field", FieldDefine.class, "addField");
			this.addObjectCreate(digester, "cache_entity/constructor", ConstructorDefine.class, "addConstructor");
			this.addObjectCreate(digester, "cache_entity/constructor/constructor_arg", ConstructorArgDefine.class, "addField");
		}
	},

	REDIS_ENTITY("RedisEntity.xsd") {
		@Override
		public void initDigester(Digester digester) {
			this.addObjectCreate(digester, "redis_entity", RedisEntityDefine.class, "setEntityDefine");
			this.addObjectCreate(digester, "redis_entity/field", FieldDefine.class, "addField");
			this.addObjectCreate(digester, "redis_entity/constructor", ConstructorDefine.class, "addConstructor");
			this.addObjectCreate(digester, "redis_entity/constructor/constructor_arg", ConstructorArgDefine.class, "addField");
		}
	},

	DB_ENTITY_LIST("DbEntityList.xsd") {
		@Override
		public void initDigester(Digester digester) {

		}
	},

	CACHE_ENTITY_LIST("CacheEntityList.xsd") {
		@Override
		public void initDigester(Digester digester) {

		}
	},

	REDIS_ENTITY_LIST("RedisEntityList.xsd") {
		@Override
		public void initDigester(Digester digester) {

		}
	},
	;
	private String xsdName;

	EntityType(String xsdName) {
		this.xsdName = xsdName;
	}

	/***
	 * 是不是list类型
	 * @return
	 */
	public boolean isList(){
		return name().endsWith("LIST");
	}
	/***
	 * 初始化 digester
	 * @param digester
	 */
	public abstract void initDigester(Digester digester);
	/***
	 * 返回xml的root element的name
	 * @return
	 */
	public String getXmlRootElementName(){
		return name().toLowerCase();
	}

	/***
	 * 校验对应的xml
	 * @param is
	 */
	public void validate(InputStream is) throws IOException, SAXException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL url = Thread.currentThread().getContextClassLoader().getResource("xsd/"+xsdName);
		assert url != null;
		Schema schema = factory.newSchema(url);
		schema.newValidator().validate(new StreamSource(is));
	}

	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	public static EntityType parse(File xmlFile) {
		if (! xmlFile.isFile() || ! xmlFile.getName().endsWith(".xml")) {
			throw new RuntimeException("file ["+xmlFile.getAbsolutePath()+"] is not xml File");
		}
		String rootElementName = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			Element element = document.getDocumentElement();
			rootElementName = element.getNodeName();

			for (EntityType type : values()) {
				if (type.getXmlRootElementName().equals(rootElementName)) {
					return type;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("file ["+xmlFile.getAbsolutePath()+"] start with ["+rootElementName+"] is not entity xml File");
	}

	/**
	 * 对digester处理
	 * @param pattern xml 路径内容
	 * @param clazz 对应的class
	 * @param setNext 往上层路径传入的方法
	 */
	protected void addObjectCreate(Digester digester,String pattern, Class<?> clazz, String setNext) {
		digester.addObjectCreate(pattern, clazz);
		digester.addSetProperties(pattern);
		digester.addSetNext(pattern,setNext);
	}
}
