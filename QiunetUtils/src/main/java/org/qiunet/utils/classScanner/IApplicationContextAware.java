package org.qiunet.utils.classScanner;

/***
 * 实现接口必须是空构造函数.
 */
public interface IApplicationContextAware {
	/***
	 *
	 * @param context
	 */
	void setApplicationContext(IApplicationContext context);
}
