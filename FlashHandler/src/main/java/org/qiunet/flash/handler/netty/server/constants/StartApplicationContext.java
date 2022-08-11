package org.qiunet.flash.handler.netty.server.constants;

import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 * @author qiunet
 * 2022/8/11 15:55
 */
class StartApplicationContext implements IApplicationContextAware {
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		ServerConstants.startDt.compareAndSet(0, System.currentTimeMillis());
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.NONE;
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}
}
