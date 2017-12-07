package org.qiunet.test.params;

import org.qiunet.test.executor.IExecutorInitializer;
import org.qiunet.test.robot.init.IRobotFactory;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.classScanner.IScannerHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by qiunet.
 * 17/12/6
 */
public class ExecutorParams {
	private List<ITestCase> testCases;
	private IRobotFactory robotFactory;
	private IExecutorInitializer initializer;
	private List<IScannerHandler> scannerHandlers;

	private ExecutorParams (){}

	public List<IScannerHandler> getScannerHandlers() {
		return scannerHandlers;
	}

	public IExecutorInitializer getInitializer() {
		return initializer;
	}

	public IRobotFactory getRobotFactory() {
		return robotFactory;
	}

	public List<ITestCase> getTestCases() {
		return testCases;
	}

	public static Build custom(){
		return new Build();
	}

	public static class Build {
		private Build(){}
		private IRobotFactory robotFactory;
		private IExecutorInitializer initializer;
		private List<IScannerHandler> scannerHandlers = new ArrayList<>(3);
		private List<ITestCase> testCases = new ArrayList<>(128);

		public Build setInitializer(IExecutorInitializer initializer) {
			this.initializer = initializer;
			return this;
		}

		public Build addTestCase(ITestCase testCase) {
			this.testCases.add(testCase);
			return this;
		}

		public Build addScannerHandler(IScannerHandler scannerHandler) {
			this.scannerHandlers.add(scannerHandler);
			return this;
		}
		public Build setRobotFactory(IRobotFactory robotFactory) {
			this.robotFactory = robotFactory;
			return this;
		}

		public ExecutorParams build(){
			ExecutorParams params = new ExecutorParams();
			params.robotFactory = this.robotFactory;
			params.initializer = this.initializer;
			params.testCases = Collections.unmodifiableList(testCases);
			params.scannerHandlers = Collections.unmodifiableList(scannerHandlers);
			return params;
		}
	}
}
