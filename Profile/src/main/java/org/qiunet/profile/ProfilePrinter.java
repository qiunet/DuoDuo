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
public class ProfilePrinter<Key, Column extends Enum<Column> & IProColumn> {

	private Profile<Key, Column> profile;

	private ProfilePrinter(Profile<Key, Column> profile) {
		this.profile = profile;
	}

	public static <Key, Column extends Enum<Column> & IProColumn> ProfilePrinter<Key, Column> valueOf(Profile<Key, Column> profile){
		return new ProfilePrinter<>(profile);
	}

	/***
	 * 打印
	 * @param printer
	 */
	public void print(PrintStream printer) {
		TextTable<ProRowInfo<Key, Column>> textTable = new TextTable<>();
		textTable.setDataList(profile.toList());
		textTable.addColumnInfo(new TextTableColumnInfo<>("key", ProRowInfo::getKey));
		for (Column column : profile.getColumns()) {
			textTable.addColumnInfo(new TextTableColumnInfo<>(column.name(), row -> row.getValue(column.ordinal())));
		}

		textTable.print(printer);
		printer.print("UseTime:");
		printer.print((System.currentTimeMillis() - profile.getStartDt()));
		printer.println("ms");
	}
}
