package org.qiunet.flash.handler.common.protobuf;

import com.google.protobuf.CodedOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.CharsetUtil;
import org.qiunet.utils.pool.ThreadScopeObjectPool;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UTFDataFormatException;

/***
 * 自定义的可以缓存 CodedOutputStream 的线程变量类.
 *
 * @author qiunet
 * 2022/8/19 15:23
 */
class CodedOutputStreamThreadCache extends OutputStream implements DataOutput {

	private static final ThreadScopeObjectPool<CodedOutputStreamThreadCache> pool = new ThreadScopeObjectPool<>(CodedOutputStreamThreadCache::new);

	private final CodedOutputStream codedOutputStream = CodedOutputStream.newInstance(this, 0);
	private ByteBuf buffer;

	private CodedOutputStreamThreadCache(){}


	static CodedOutputStreamThreadCache get() {
		CodedOutputStreamThreadCache obj = pool.get();
		obj.buffer = PooledByteBufAllocator.DEFAULT.buffer();
		return obj;
	}

	public CodedOutputStream getCodedOutputStream() {
		return codedOutputStream;
	}

	public ByteBuf recycle() {
		try {
			this.codedOutputStream.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ByteBuf temp = this.buffer;
		this.buffer = null;
		pool.recycle(this);
		return temp;
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		this.buffer.writeBoolean(v);
	}

	@Override
	public void writeByte(int v) throws IOException {
		this.buffer.writeByte(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		this.buffer.writeShort(v);
	}

	@Override
	public void writeChar(int v) throws IOException {
		this.buffer.writeChar(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		this.buffer.writeInt(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		this.buffer.writeLong(v);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		this.buffer.writeFloat(v);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		this.buffer.writeDouble(v);
	}

	@Override
	public void writeBytes(String s) throws IOException {
		this.buffer.writeCharSequence(s, CharsetUtil.US_ASCII);
	}

	@Override
	public void writeChars(String s) throws IOException {
		int len = s.length();

		for(int i = 0; i < len; ++i) {
			this.buffer.writeChar(s.charAt(i));
		}
	}

	@Override
	public void write(int b) throws IOException {
		this.buffer.writeByte(b);
	}

	@Override
	public void writeUTF(String str) throws IOException {
		int strlen = str.length();
		int utflen = 0;
		int c, count = 0;

		/* use charAt instead of copying String to char array */
		for (int i = 0; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}

		if (utflen > 65535)
			throw new UTFDataFormatException("encoded string too long: " + utflen + " bytes");

		byte[]  bytearr = new byte[utflen+2];
		bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
		bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);

		int i=0;
		for (i=0; i<strlen; i++) {
			c = str.charAt(i);
			if (!((c >= 0x0001) && (c <= 0x007F))) break;
			bytearr[count++] = (byte) c;
		}

		for (;i < strlen; i++){
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				bytearr[count++] = (byte) c;

			} else if (c > 0x07FF) {
				bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				bytearr[count++] = (byte) (0x80 | ((c >>  6) & 0x3F));
				bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			} else {
				bytearr[count++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
				bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			}
		}
		this.write(bytearr, 0, utflen+2);
	}

}
