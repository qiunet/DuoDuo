package org.qiunet.flash.handler.bootstrap;

import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.profile.ExternalProfiler;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.qiunet.flash.handler.netty.server.BootstrapServer;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

/***
 *
 *
 * @author qiunet
 * 2020-11-18 11:58
 */
public class HttpJmhProfile implements ExternalProfiler {

	@Override
	public Collection<String> addJVMInvokeOptions(BenchmarkParams params) {
		return Collections.emptyList();
	}

	@Override
	public Collection<String> addJVMOptions(BenchmarkParams params) {
		return Collections.emptyList();
	}

	@Override
	public void beforeTrial(BenchmarkParams benchmarkParams) {
	}

	@Override
	public Collection<? extends Result> afterTrial(BenchmarkResult br, long pid, File stdOut, File stdErr) {
		BootstrapServer.sendHookMsg(HttpRequestProfile.hook.getHookPort(), HttpRequestProfile.hook.getShutdownMsg());
		return Collections.emptyList();
	}

	@Override
	public boolean allowPrintOut() {
		return true;
	}

	@Override
	public boolean allowPrintErr() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Qiunet-----Profile=-------";
	}
}
