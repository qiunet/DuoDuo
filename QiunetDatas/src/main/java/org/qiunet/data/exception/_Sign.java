package org.qiunet.data.exception;

import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;
import org.qiunet.utils.classScanner.Singleton;
import org.qiunet.utils.encryptAndDecrypt.StrCodecUtil;
import org.qiunet.utils.logger.LoggerType;

/***
 * 多多签名
 *
 * @author qiunet
 * 2020/3/1 21:13
 **/
@Singleton
public class _Sign implements IApplicationContextAware {
	private static final String sign = "546869732069732044756f44756f2c2043726561746564206279207169756e6574";
	@Override
	public void setApplicationContext(IApplicationContext context) throws Exception {
		LoggerType.DUODUO.getLogger().trace(StrCodecUtil.decrypt(sign));
	}
}
