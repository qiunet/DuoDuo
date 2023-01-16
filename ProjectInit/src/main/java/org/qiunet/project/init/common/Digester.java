package org.qiunet.project.init.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/***
 * 解析 xml 并根据规则生成对应的对象.!
 *
 * @author qiunet
 * 2023/1/14 08:32
 */
public class Digester {
	private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	/**
	 * path to  node
	 */
	private final Map<String, DigesterNode> allNode = Maps.newHashMap();
	/**
	 * 最终要set到的对象
	 */
	private final Object rootObj;

	public Digester(Object rootObj) {
		this.rootObj = rootObj;
	}

	public void parse(File file) throws IOException, SAXException, ParserConfigurationException {
		DigesterNode rootNode = this.handleRelation();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(file);
		assert rootNode != null;
		rootNode.parse(rootObj, document.getDocumentElement());
	}

	/**
	 * 处理各个node的关系
	 * @return
	 */
	private DigesterNode handleRelation() {
		DigesterNode rootNode = null;
		List<String> strings = Lists.newArrayList(allNode.keySet());
		for (DigesterNode node : allNode.values()) {
			String parentPath = null;
			for (String string : strings) {
				if (string.equals(node.xmlPath())) {
					continue;
				}
				if (node.xmlPath().contains(string) && (parentPath == null || string.contains(parentPath))) {
					parentPath = string;
				}
			}
			if (parentPath == null) {
				rootNode = node;
			}else {
				node.parentNode = allNode.get(parentPath);
				node.parentNode.children().put(node.xmlPath(), node);
			}
		}
		return rootNode;
	}

	/**
	 * 添加一个 对象生成规则.
	 * @param pattern / 隔开的xml path
	 * @param clazz 当前路径的class类型
	 * @param setMtdName 上一级父类对象的set方法名
	 */
	public void addObjectCreate(String pattern, Class<?> clazz, String setMtdName) {
		this.allNode.put(pattern, new DigesterNode(pattern, clazz, setMtdName));
	}
}
