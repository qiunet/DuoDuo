package org.qiunet.utils.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
	 * @param arrays
	 * @return
	 */
	public static <T> boolean existInList(T element,T ... arrays)
	{
		if (arrays == null || element == null) {
			return false;
		}
		return Arrays.asList(arrays).contains(element);
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
		if(list.isEmpty() || element == null) {
			return false;
		}
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
