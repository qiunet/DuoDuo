package org.qiunet.utils.file;

import java.io.File;

/***
 * 文件变动. 执行的操作.
 */
public interface IFileChangeCallback {
	/***
	 *
	 * @param file
	 */
	void call(File file);
}
