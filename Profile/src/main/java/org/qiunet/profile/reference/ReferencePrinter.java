package org.qiunet.profile.reference;

import org.qiunet.profile.printer.TextTable;
import org.qiunet.profile.printer.TextTableColumnInfo;

import java.io.PrintStream;

/***
 * 参考数据打印器
 *
 * @author qiunet
 * 2022/8/25 21:15
 */
public class ReferencePrinter<Key> {

	private final ReferenceData<Key> data;

	ReferencePrinter(ReferenceData<Key> data) {
		this.data = data;
	}

	void print(PrintStream printer) {
		TextTable<ReferenceRow<Key>> textTable = new TextTable<>();
		textTable.setDataList(data.toList())
		.addColumnInfo(new TextTableColumnInfo<>("key", r -> r.getKey().toString()))
		.addColumnInfo(new TextTableColumnInfo<>("max", r -> String.valueOf(r.getMax())))
		.addColumnInfo(new TextTableColumnInfo<>("min", r -> String.valueOf(r.getMin())))
		.addColumnInfo(new TextTableColumnInfo<>("count", r -> String.valueOf(r.getCount())))
		.addColumnInfo(new TextTableColumnInfo<>("total", r -> String.valueOf(r.getTotal())))
		.addColumnInfo(new TextTableColumnInfo<>("avg", r -> String.valueOf(r.getAvg())));

		textTable.print(printer);
		printer.println("Use Time:"+(System.currentTimeMillis() - data.getStartDt())+"ms");
	}
}
