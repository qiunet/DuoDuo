package org.qiunet.project.init.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.utils.convert.ConvertManager;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.string.StringUtil;
import org.w3c.dom.Element;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static org.w3c.dom.Node.ELEMENT_NODE;

/***
 * 一个规则节点.
 * @author qiunet
 * 2023/1/16 10:54
 */
class DigesterNode {
	/**
	 * 子节点
	 */
	private final Map<String, DigesterNode> children = Maps.newHashMap();
	/**
	 * 父节点
	 */
	DigesterNode parentNode;
	/**
	 * 自己的名
	 */
	private final String xmlPath;
	/**
	 * 往父类set的方法名
	 */
	private final String setMtdName;
	/**
	 * 自己的clz
	 */
	private final Class<?> clz;

	DigesterNode(String xmlPath, Class<?> clz, String setMtdName) {
		this.setMtdName = setMtdName;
		this.xmlPath = xmlPath;
		this.clz = clz;
	}

	/**
	 * 解析并设置值
	 * @param parent
	 * @param element
	 */
	public void parse(Object parent, Element element) {
		if (elementPath(element).equals(this.xmlPath)) {
			Object obj = this.handlerSelf(parent, element);
			this.handleChildren(obj, element);
			return;
		}

		this.handleChildren(parent, element);
	}

	/**
	 * 处理自身
	 * @param parent
	 * @param element
	 */
	private Object handlerSelf(Object parent, Element element) {
		try {
			Object instance = getClz().getDeclaredConstructor().newInstance();

			Class<?> searchType = getClz();
			while (Object.class != searchType && searchType != null) {
				Field[] fields = ReflectUtil.getDeclaredFields(searchType);
				for (Field field : fields) {
					String name = field.getName();
					String value = element.getAttribute(name);
					if (!StringUtil.isEmpty(value)) {
						Object val = ConvertManager.instance.convert(field, value);
						ReflectUtil.setField(instance, name, val);
					}
				}
				searchType = searchType.getSuperclass();
			}

			Method method = Arrays.stream(parent.getClass().getDeclaredMethods())
					.filter(mtd -> mtd.getName().equals(setMtdName))
					.filter(mtd -> mtd.getParameterTypes()[0].isAssignableFrom(getClz()))
					.filter(mtd -> mtd.getParameterCount() == 1)
					.findFirst().orElse(null);
			if (method != null) {
				method.invoke(parent, instance);
			}
			return instance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * 计算element 的 path
	 * @param element
	 * @return
	 */
	private String elementPath(org.w3c.dom.Node element) {
		List<String> list = Lists.newArrayList();
		while (element != null && element.getNodeType() == ELEMENT_NODE) {
			list.add(element.getNodeName());
			element = element.getParentNode();
		}
		StringJoiner joiner = new StringJoiner("/");
		for (int i = list.size() - 1; i >= 0; i--) {
			joiner.add(list.get(i));
		}
		return joiner.toString();
	}

	/**
	 * 处理子节点
	 */
	private void handleChildren(Object parent, Element node) {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			org.w3c.dom.Node item = node.getChildNodes().item(i);
			if (item.getNodeType() != ELEMENT_NODE) {
				continue;
			}
			DigesterNode digesterNode = children.get(elementPath(item));
			if (digesterNode == null) {
				handleChildren(parent, (Element) item);
				return;
			}
			digesterNode.parse(parent, (Element) item);
		}
	}

	public Map<String, DigesterNode> children() {
		return children;
	}

	public String xmlPath() {
		return xmlPath;
	}

	public Class<?> getClz() {
		return clz;
	}

	@Override
	public String toString() {
		return xmlPath;
	}
}
