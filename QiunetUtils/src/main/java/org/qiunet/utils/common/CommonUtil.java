package org.qiunet.utils.common;

import java.util.*;

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
		for(T t : list)
		{
			if(element.equals(t))
			{
				return true;
			}
		}
		return false;
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
		if(list.isEmpty()) return false;
		for(T t : list)
		{
			if(element.equals(t))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 截取某列表的部分数据
	 * @param list 原始list
	 * @param skip 从skip 开始
	 * @param count 截取数
	 * @return
	 */
	public static <T> List<T> getSubListPage(List<T> list, int skip , int count) {
		if(list==null||list.isEmpty()){
			return null;
		}
		int startIndex = skip;
		int endIndex = skip+count;
		if(startIndex>endIndex||startIndex>list.size()){
			return null;
		}
		if(endIndex>list.size()){
			endIndex = list.size();
		}
		return list.subList(startIndex, endIndex);
	}
}
