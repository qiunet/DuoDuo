package org.qiunet.utils.logger;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.qiunet.utils.logger.base.TheGameLoggerUtil;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 日志记录 不打印回车 换行 
 * @author qiunet
 *         Created on 17/1/6 11:30.
 */
public class GameConsoleAppender extends ConsoleAppender {
	/**
	 * Constructs an unconfigured appender.
	 */
	public GameConsoleAppender() {
		super();
	}
	
	/**
	 * Creates a configured appender.
	 *
	 * @param layout layout, may not be null.
	 */
	public GameConsoleAppender(Layout layout)
	{
		super(layout);
	}
	
	/**
	 *   Creates a configured appender.
	 * @param layout layout, may not be null.
	 * @param target target, either "System.err" or "System.out".
	 */
	public GameConsoleAppender(Layout layout, String target) {
		super(layout, target);
	}
	
	
	/**
	 *   Prepares the appender for use.
	 */
	public void activateOptions() {
		if (target.equals(SYSTEM_ERR)) {
			setWriter(createWriter(new GameConsoleAppender.SystemErrStream()));
		} else {
			setWriter(createWriter(new GameConsoleAppender.SystemOutStream()));
		}
		// super.activateOptions();
	}
	/**
	 * An implementation of OutputStream that redirects to the
	 * current System.err.
	 *
	 */
	private static class SystemErrStream extends OutputStream {
		public SystemErrStream() {
		}
		
		public void close() {
		}
		
		public void flush() {
			System.err.flush();
		}
		
		public void write(final byte[] b) throws IOException {
			System.err.write(TheGameLoggerUtil.trimLineBreak(b, 0, b.length));
		}
		
		public void write(final byte[] b, final int off, final int len)
				throws IOException {
			System.err.write(TheGameLoggerUtil.trimLineBreak(b, off, len), off, len);
		}
		
		public void write(final int b) throws IOException {
			System.err.write(b);
		}
	}
	
	/**
	 * An implementation of OutputStream that redirects to the
	 * current System.out.
	 *
	 */
	private static class SystemOutStream extends OutputStream {
		public SystemOutStream() {
		}
		
		public void close() {
		}
		
		public void flush() {
			System.out.flush();
		}
		
		public void write(final byte[] b) throws IOException {
			System.out.write(TheGameLoggerUtil.trimLineBreak(b, 0, b.length));
		}
		
		public void write(final byte[] b, final int off, final int len)
				throws IOException {
			System.out.write(TheGameLoggerUtil.trimLineBreak(b, off, len), off, len);
		}
		
		public void write(final int b) throws IOException {
			System.out.write(b);
		}
	}
	
	protected void subAppend(LoggingEvent event) {
		if (TheGameLoggerUtil.AppenderLoggerToThreadLocal(getName(), event)){
			super.subAppend(event);
		}
	}
}
