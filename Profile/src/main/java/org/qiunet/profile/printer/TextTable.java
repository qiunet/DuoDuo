package org.qiunet.profile.printer;

import com.google.common.collect.Lists;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.string.StringUtil;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

/***
 * 数据
 *
 * @author qiunet
 * 2020-11-04 14:46
 */
public class TextTable<O> {
	/**
	 * 所有数据
	 */
	private List<O> dataList;
	/**
	 * 排序
	 */
	private Comparator<O> comparator;
	/**
	 * 列信息
	 */
	private final List<TextTableColumnInfo<O>> columnInfos = Lists.newArrayList();


	public TextTable<O> setDataList(List<O> dataList) {
		this.dataList = dataList;
		return this;
	}

	public TextTable<O> setComparator(Comparator<O> comparator) {
		this.comparator = comparator;
		return this;
	}

	public TextTable<O> addColumnInfo(TextTableColumnInfo<O> columnInfo) {
		this.columnInfos.add(columnInfo);
		return this;
	}

	/***
	 * 打印
	 * @param printer
	 */
	public void print(PrintStream printer) {
		if (dataList == null) {
			throw new CustomException("data list is empty!");
		}

		if (comparator != null) {
			dataList.sort(comparator);
		}

		for (TextTableColumnInfo<O> columnInfo : columnInfos) {
			dataList.forEach(columnInfo::addData);
		}

		String separate = this.buildRowSeparate();
		for (int i = 0; i <= dataList.size(); i++) {
			printer.println(separate);
			StringBuilder sb = new StringBuilder(128);
			for (TextTableColumnInfo<O> columnInfo : columnInfos) {
				TextTableColumnValue columnValue = columnInfo.getColumnValue(i);
				sb.append("|").append(columnValue.getValue()).append(StringUtil.repeated(" ", columnInfo.getMaxLength() - columnValue.getLength()));
			}
			sb.append("|");
			printer.println(sb.toString());
		}
		printer.println(separate);
	}

	/**
	 * 分割线
	 * @return
	 */
	private String buildRowSeparate() {
		StringJoiner sb = new StringJoiner("+", "+", "+");
		for (TextTableColumnInfo<O> columnInfo : columnInfos) {
			sb.add(StringUtil.repeated("-", columnInfo.getMaxLength()));
		}
		return sb.toString();
	}
}
