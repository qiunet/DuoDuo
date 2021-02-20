package org.qiunet.excel2cfgs.swing.enums;

/***
 * 工具栏按钮类型
 * @Author qiunet
 * @Date 2021/2/10 09:39
 **/
public enum IconButtonType {
	/**
	 * 设定转换
	 */
	cfg("Excel配置转换"),
	/**
	 * json
	 */
	json("Json格式化"),
	/**
	 * 时间戳转换
	 */
	time("时间戳转换"),
	/**
	 * 设置
	 */
	setting("设置"),
	;
	private String title;

	IconButtonType(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
