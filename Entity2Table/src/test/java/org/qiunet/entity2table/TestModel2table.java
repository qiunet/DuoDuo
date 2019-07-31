package org.qiunet.entity2table;

import org.qiunet.entity2table.annotation.InitData;
import org.qiunet.entity2table.controller.CreateTableController;

/**
 * Created by zhengj
 * Date: 2019/7/26.
 * Time: 15:42.
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
