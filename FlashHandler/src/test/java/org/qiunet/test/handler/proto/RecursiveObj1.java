package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.util.List;

/***
 *
 * @author qiunet
 * 2022/2/9 11:14
 */

public class RecursiveObj1 {
	@Protobuf
	private List<RecursiveObj2> a;

	public List<RecursiveObj2> getA() {
		return a;
	}

	public void setA(List<RecursiveObj2> a) {
		this.a = a;
	}
}
