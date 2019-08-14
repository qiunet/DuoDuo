package org.qiunet.project.init.enums;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
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

	DB_ENTITY("DbEntity.xsd"),

	CACHE_ENTITY("CacheEntity.xsd"),

	REDIS_ENTITY("RedisEntity.xsd"),

	DB_ENTITY_LIST("DbEntityList.xsd"),

	CACHE_ENTITY_LIST("CacheEntityList.xsd"),

	REDIS_ENTITY_LIST("RedisEntityList.xsd"),
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
	public void validate(InputStream is) {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL url = Thread.currentThread().getContextClassLoader().getResource("xsd/"+xsdName);
		assert url != null;
		try {
			Schema schema = factory.newSchema(url);
			schema.newValidator().validate(new StreamSource(is));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
