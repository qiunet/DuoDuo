package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.util.Map;

/***
 *
 * @author qiunet
 * 2022/2/9 11:14
 */

public class RecursiveObj3 {
	@Protobuf
	private Map<String, RecursiveObj2> a;
	@Protobuf
	private Map<String, Integer> b;

	public Map<String, RecursiveObj2> getA() {
		return a;
	}

	public void setA(Map<String, RecursiveObj2> a) {
		this.a = a;
	}

	public Map<String, Integer> getB() {
		return b;
	}

	public void setB(Map<String, Integer> b) {
		this.b = b;
	}
}
