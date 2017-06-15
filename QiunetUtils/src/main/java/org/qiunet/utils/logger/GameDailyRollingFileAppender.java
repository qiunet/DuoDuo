package org.qiunet.utils.logger;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.qiunet.utils.logger.base.GameFileOutPutStream;
import org.qiunet.utils.logger.base.TheGameLoggerUtil;

import java.io.*;

/**
 * @author qiunet
 *         Created on 17/1/9 08:30.
 */
public class GameDailyRollingFileAppender extends DailyRollingFileAppender {
	/**
	 The default constructor does nothing. */
	public GameDailyRollingFileAppender() {
		super();
	}
	
	/**
	 Instantiate a <code>DailyRollingFileAppender</code> and open the
	 file designated by <code>filename</code>. The opened filename will
	 become the ouput destination for this appender.
	 
	 */
	public GameDailyRollingFileAppender (Layout layout, String filename, String datePattern) throws IOException {
		super(layout, filename, datePattern);
	}
	
	/**
	 <p>Sets and <i>opens</i> the file where the log output will
	 go. The specified file must be writable.
	 
	 <p>If there was already an opened file, then the previous file
	 is closed first.
	 
	 <p><b>Do not use this method directly. To configure a FileAppender
	 or one of its subclasses, set its properties one by one and then
	 call activateOptions.</b>
	 
	 @param fileName The path to the log file.
	 @param append   If true will append to fileName. Otherwise will
	 truncate fileName.  */
	public
	synchronized
	void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
			throws IOException {
		LogLog.debug("setFile called: "+fileName+", "+append);
		
		// It does not make sense to have immediate flush and bufferedIO.
		if(bufferedIO) {
			setImmediateFlush(false);
		}
		
		reset();
		FileOutputStream ostream = null;
		try {
			//
			//   attempt to create file
			//
			ostream = new GameFileOutPutStream(fileName, append);
		} catch(FileNotFoundException ex) {
			//
			//   if parent directory does not exist then
			//      attempt to create it and try to create file
			//      see bug 9150
			//
			String parentName = new File(fileName).getParent();
			if (parentName != null) {
				File parentDir = new File(parentName);
				if(!parentDir.exists() && parentDir.mkdirs()) {
					ostream = new FileOutputStream(fileName, append);
				} else {
					throw ex;
				}
			} else {
				throw ex;
			}
		}
		Writer fw = createWriter(ostream);
		if(bufferedIO) {
			fw = new BufferedWriter(fw, bufferSize);
		}
		this.setQWForFiles(fw);
		this.fileName = fileName;
		this.fileAppend = append;
		this.bufferedIO = bufferedIO;
		this.bufferSize = bufferSize;
		writeHeader();
		LogLog.debug("setFile ended");
	}
	
	
	protected void subAppend(LoggingEvent event) {
		if (TheGameLoggerUtil.AppenderLoggerToThreadLocal(getName(), event)) {
			super.subAppend(event);
		}
	}
}
