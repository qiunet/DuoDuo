package org.qiunet.entity2table;

import org.qiunet.data.core.support.db.event.DbLoaderOverEvent;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/**
 * Created by zhengj
 * Date: 2019/7/26.
 * Time: 15:42.
 * To change this template use File | Settings | File Templates.
 */
public class TestModel2table {

	public static void main(String[] args) {
		ClassScanner.getInstance(ScannerType.CREATE_TABLE, ScannerType.AUTO_WIRE).scanner();
		new DbLoaderOverEvent().fireEventHandler();
	}
}
