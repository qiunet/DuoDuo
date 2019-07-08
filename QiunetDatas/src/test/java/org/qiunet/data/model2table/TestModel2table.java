package org.qiunet.data.model2table;

import org.qiunet.data.model2table.annotation.InitData;
import org.qiunet.data.model2table.controller.CreateTableController;

/**
 * Created by zhengj
 * Date: 2019/4/22.
 * Time: 22:44.
 * To change this template use File | Settings | File Templates.
 */
public class TestModel2table {
	@InitData
	public void initData() {
		System.out.println("\n\n\n===================TestOne");
	}

	public static void main(String[] args) {
		CreateTableController createTableController = new CreateTableController();
		createTableController.start();
	}

}
