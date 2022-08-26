package org.qiunet.profile.printer;

import com.google.common.collect.Lists;
import org.qiunet.utils.string.StringUtil;

import java.util.List;
import java.util.function.Function;

/***
 * 打印Table的列信息
 *
 * @author qiunet
 * 2020-11-04 14:41
 */
public class TextTableColumnInfo<O> {
	/**
	 * 该列最大宽度
	 */
	private int maxLength;

	private final Function<O, String> getter;
	/**
	 * 该列的数据
	 */
	private final List<TextTableColumnValue> columnData = Lists.newArrayList();

	public TextTableColumnInfo(String name, Function<O, String> getter) {
		this.getter = getter;
		this.maxLength = StringUtil.getMixedStringLength(name);
		this.columnData.add(new TextTableColumnValue(name, maxLength));
	}

	/**
	 * 添加一次数据
	 * @param data
	 */
	void addData(O data) {
		String columnValue = getter.apply(data);
		int length = StringUtil.getMixedStringLength(columnValue);
		this.columnData.add(new TextTableColumnValue(columnValue, length));
		this.maxLength = Math.max(this.maxLength, length);
	}

	TextTableColumnValue getColumnValue(int i) {
		return columnData.get(i);
	}

	int getMaxLength() {
		return maxLength + 1;
	}
}
