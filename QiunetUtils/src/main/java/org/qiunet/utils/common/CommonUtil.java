package org.qiunet.utils.common;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 其它可以通用逻辑的工具类
 * @author qiunet
 *
 */
public class CommonUtil {

	private CommonUtil() { }
	/**
	 * 检查一个元素是否在数组中
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> boolean existInList(T element,T list[])
	{
		if (list == null || element == null) return false;
		return Stream.of(list).anyMatch(l -> l.equals(element));
	}
	/**
	 * 检查一个元素是否在集合中
	 * @param <T>
	 * @param element
	 * @param list
	 * @return
	 */
	public static <T> boolean existInList(T element,Collection<T> list)
	{
		if(list.isEmpty() || element == null) return false;
		return list.stream().anyMatch(ele -> ele.equals(element));
	}
	/**
	 * 截取某列表的部分数据
	 * @param list 原始list
	 * @param skip 从skip 开始
	 * @param count 截取数
	 * @return
	 */
	public static <T> List<T> getSubListPage(List<T> list, int skip , int count) {
		if(list==null || list.isEmpty()){
			return null;
		}
		return list.stream().skip(skip).limit(count).collect(Collectors.toList());
	}
}
