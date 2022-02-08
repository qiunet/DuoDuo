package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/***
 *
 * @author qiunet
 * 2022/2/9 11:14
 */

public class RecursiveObj2 {
	@Protobuf
	private int a;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}
}
