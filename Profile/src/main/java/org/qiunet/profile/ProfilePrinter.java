package org.qiunet.profile;

import org.qiunet.profile.printer.TextTable;
import org.qiunet.profile.printer.TextTableColumnInfo;

import java.io.PrintStream;

/***
 *
 *
 * @author qiunet
 * 2020-11-04 12:36
 */
 class ProfilePrinter<Key, Column extends Enum<Column> & IProColumn> {

	private final Profile<Key, Column> profile;

	private ProfilePrinter(Profile<Key, Column> profile) {
		this.profile = profile;
	}

	static <Key, Column extends Enum<Column> & IProColumn> ProfilePrinter<Key, Column> valueOf(Profile<Key, Column> profile){
		return new ProfilePrinter<>(profile);
	}

	/***
	 * 打印
	 * @param printer
	 */
	void print(PrintStream printer) {
		TextTable<ProRowInfo<Key, Column>> textTable = new TextTable<>();
		textTable.setDataList(profile.toList())
		.addColumnInfo(new TextTableColumnInfo<>("key", ProRowInfo::getKey));
		for (Column column : profile.getColumns()) {
			TextTableColumnInfo<ProRowInfo<Key, Column>> columnInfo =
				new TextTableColumnInfo<>(column.name(), row -> row.getValue(column.ordinal()));

			textTable.addColumnInfo(columnInfo);
		}

		textTable.print(printer);
		printer.println("Use Time:"+(System.currentTimeMillis() - profile.getStartDt())+"ms");
	}
}
