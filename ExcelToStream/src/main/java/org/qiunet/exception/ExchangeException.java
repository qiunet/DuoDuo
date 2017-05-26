package org.qiunet.exception;

/**
 * Created by qiunet.
 * 17/5/26
 */
public class ExchangeException extends RuntimeException {

	public ExchangeException (int rowNum,int columnNum) {
		super("行数 ["+rowNum+"] 列数 ["+columnNum+"] 数据格式不匹配!");
	}
}
