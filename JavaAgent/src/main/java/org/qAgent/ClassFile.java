package org.qAgent;

import java.io.*;

/***
 * 内部使用。 不提供给外部。
 *
 * @author qiunet
 * 2021/10/18 09:59
 **/
public final class ClassFile {
	private String className;

	public ClassFile(File classFile) {
		try (FileInputStream fis = new FileInputStream(classFile);
			DataInputStream dis = new DataInputStream(fis)){
			this.read(dis);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ClassFile(InputStream stream) {
		try (DataInputStream dis = new DataInputStream(stream)){
			this.read(dis);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void read(DataInputStream in) throws IOException {
		int magic = in.readInt();
		if (magic != 0xCAFEBABE)
			throw new IOException("bad magic number: " + Integer.toHexString(magic));
		//major
		in.readUnsignedShort();
		in.readUnsignedShort();

		ConstPool constPool = new ConstPool(in);

		in.readUnsignedShort();
		int thisClass = in.readUnsignedShort();
		constPool.setThisClassInfo(thisClass);
		in.readUnsignedShort();

		className = constPool.getClassInfo(thisClass);
	}

	public String getClassName() {
		return className;
	}
}
