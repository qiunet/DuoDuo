package org.qiunet.excel2cfgs.common.exception;

/**
 * Created by qiunet.
 * 17/5/26
 */
public class ExchangeException extends RuntimeException {

	public ExchangeException (String outFileName, int rowNum,int columnNum, String message) {
		super("文件 ["+outFileName+"] 行数 ["+rowNum+"] 列数 ["+columnNum+"] 数据格式不匹配!" + message);
	}
}
