package org.qiunet.utils.scanner;

import org.qiunet.utils.args.ArgsContainer;

/***
 * 实现接口必须是空构造函数.
 * @author qiunet
 */
public interface IApplicationContextAware {
	/**
	 * 可以传入一些参数给context
	 * 有需要.可以自己覆盖该方法.
	 * @param context
	 * @throws Exception
	 */
	void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception;

	/**
	 * 越大执行越靠前
	 * @return
	 */
	default int order() {
		return 0;
	}

	/**
	 * 部分只有某个场合执行.
	 * 做下区分
	 * @return
	 */
	default ScannerType scannerType() {
		return ScannerType.ALL;
	}
}
