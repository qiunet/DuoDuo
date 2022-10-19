package org.qiunet.flash.handler.common.protobuf;

import com.google.protobuf.CodedInputStream;
import org.qiunet.utils.pool.ThreadScopeObjectPool;
import org.qiunet.utils.reflect.ReflectUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/***
 * 自定义的可以缓存 CodedOutputStream 的线程变量类.
 *
 * @author qiunet
 * 2022/8/19 15:23
 */
class CodedInputStreamThreadCache extends InputStream {

	private static final ThreadScopeObjectPool<CodedInputStreamThreadCache> pool = new ThreadScopeObjectPool<>(CodedInputStreamThreadCache::new);

	private final CodedInputStream codedInputStream = CodedInputStream.newInstance(this);

	private final Field field = ReflectUtil.makeAccessible(ReflectUtil.findField(codedInputStream.getClass(), "totalBytesRetired"));

	private ByteBuffer buffer;

	private CodedInputStreamThreadCache(){}


	static CodedInputStreamThreadCache get(ByteBuffer byteBuffer) {
		CodedInputStreamThreadCache obj = pool.get();
		obj.buffer = byteBuffer;
		return obj;
	}

	public CodedInputStream getCodedInputStream() {
		return codedInputStream;
	}

	/**
	 * 回收该对象
	 */
	public void recycle() {
		try {
			this.buffer = null;
			field.set(codedInputStream, 0);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}finally {
			pool.recycle(this);
		}
	}

	public int read() throws IOException {
		if (this.buffer == null) {
			throw new IOException("read on a closed InputStream");
		} else {
			return this.buffer.remaining() == 0 ? -1 : this.buffer.get() & 255;
		}
	}

	public int read(byte[] b) throws IOException {
		if (this.buffer == null) {
			throw new IOException("read on a closed InputStream");
		} else {
			return this.read(b, 0, b.length);
		}
	}

	public int read(byte[] b, int off, int len) throws IOException {
		if (this.buffer == null) {
			throw new IOException("read on a closed InputStream");
		} else if (b == null) {
			throw new NullPointerException();
		} else if (off >= 0 && len >= 0 && len <= b.length - off) {
			if (len == 0) {
				return 0;
			} else {
				int length = Math.min(this.buffer.remaining(), len);
				if (length == 0) {
					return -1;
				} else {
					this.buffer.get(b, off, length);
					return length;
				}
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public long skip(long n) throws IOException {
		if (this.buffer == null) {
			throw new IOException("skip on a closed InputStream");
		} else if (n <= 0L) {
			return 0L;
		} else {
			int nInt = (int)n;
			int skip = Math.min(this.buffer.remaining(), nInt);
			this.buffer.position(this.buffer.position() + skip);
			return (long)nInt;
		}
	}

	public int available() throws IOException {
		if (this.buffer == null) {
			throw new IOException("available on a closed InputStream");
		} else {
			return this.buffer.remaining();
		}
	}

	public void close() throws IOException {
		this.buffer = null;
	}

	public synchronized void mark(int readlimit) {
	}

	public synchronized void reset() throws IOException {
		throw new IOException("mark/reset not supported");
	}

	public boolean markSupported() {
		return false;
	}
}
