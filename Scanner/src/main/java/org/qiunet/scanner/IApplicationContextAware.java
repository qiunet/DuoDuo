package org.qiunet.scanner;

/***
 * 实现接口必须是空构造函数.
 * @author qiunet
 */
public interface IApplicationContextAware {
	/***
	 *
	 * @param context
	 */
	void setApplicationContext(IApplicationContext context) throws Exception;

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
