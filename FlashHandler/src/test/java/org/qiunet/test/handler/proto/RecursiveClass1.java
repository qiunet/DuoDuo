package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.util.List;

/***
 *
 * @author qiunet
 * 2022/2/9 11:14
 */

public class RecursiveClass1 {
	@Protobuf
	private int a;
	@Protobuf
	private long b;
	@Protobuf
	private String c;
	@Protobuf
	private List<RecursiveObj1> d;
	@Protobuf
	private RecursiveObj2 e;

	public RecursiveObj2 getE() {
		return e;
	}

	public void setE(RecursiveObj2 e) {
		this.e = e;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public long getB() {
		return b;
	}

	public void setB(long b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public List<RecursiveObj1> getD() {
		return d;
	}

	public void setD(List<RecursiveObj1> d) {
		this.d = d;
	}
}
