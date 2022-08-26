package org.qiunet.profile.printer;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/***
 *
 *
 * @author qiunet
 * 2020-11-04 18:01
 */
public class LoggerPrintStream extends PrintStream {
	private static final OutputStream DO_NOTHING_OUTPUT_STREAM = new DoNoThingOutputStream();
	private final Logger logger;

	public LoggerPrintStream(Logger logger) {
		super(DO_NOTHING_OUTPUT_STREAM);
		this.logger = logger;
	}

	@Override
	public void println(String string) {
		logger.info(string);
	}

	@Override
	public void print(String s) {
		this.println(s);
	}

	private static class DoNoThingOutputStream extends OutputStream{
		@Override
		public void write(int b) throws IOException {

		}
	}
}
