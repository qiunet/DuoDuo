package org.qiunet.utils.classLoader;

import java.util.concurrent.atomic.AtomicInteger;

public class ChangeClass {

	private AtomicInteger incr = new AtomicInteger();

	public void show(){
		//// 加载后显示 {num}_=====22222222========
		System.out.println(incr.getAndIncrement()+"_=====11111111========");
	}
}
