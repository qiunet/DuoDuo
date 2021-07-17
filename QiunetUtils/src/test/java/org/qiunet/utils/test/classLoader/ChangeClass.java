package org.qiunet.utils.test.classLoader;

import java.util.concurrent.atomic.AtomicInteger;

public class ChangeClass {

	private final AtomicInteger incr = new AtomicInteger();

	public void show(){
		//// 加载后显示 {num}_=====22222222========
		System.out.println(incr.getAndIncrement()+"_=====11111111========");
	}
}
