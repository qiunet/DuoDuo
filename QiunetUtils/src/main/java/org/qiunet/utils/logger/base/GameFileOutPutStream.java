package org.qiunet.utils.logger.base;

import java.io.*;

/**
 * @author qiunet
 *         Created on 17/1/9 08:19.
 */
public class GameFileOutPutStream extends FileOutputStream {
	public GameFileOutPutStream(String name) throws FileNotFoundException {
		super(name);
	}
	
	public GameFileOutPutStream(String name, boolean append) throws FileNotFoundException {
		super(name, append);
	}
	
	public GameFileOutPutStream(File file) throws FileNotFoundException {
		super(file);
	}
	
	public GameFileOutPutStream(File file, boolean append) throws FileNotFoundException {
		super(file, append);
	}
	
	public GameFileOutPutStream(FileDescriptor fdObj) {
		super(fdObj);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(TheGameLoggerUtil.trimLineBreak(b, off, len), off, len);
	}
	
	@Override
	public void write(int b) throws IOException {
		super.write(b);
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		super.write(TheGameLoggerUtil.trimLineBreak(b, 0, b.length));
	}
}
