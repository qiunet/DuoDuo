package org.qAgent;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/***
 * 内部使用。 不提供给外部。
 *
 * @author qiunet
 * 2021/10/18 09:59
 **/
final class ClassFile {
	private ConstPool constPool;
	private int thisClass;
	private String className;
	private int accessFlags;
	private int superClass;

	public ClassFile(File classFile) {
		try (FileInputStream fis = new FileInputStream(classFile);
			DataInputStream dis = new DataInputStream(fis)){
			this.read(dis);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void read(DataInputStream in) throws IOException {
		int i, n;
		int magic = in.readInt();
		if (magic != 0xCAFEBABE)
			throw new IOException("bad magic number: " + Integer.toHexString(magic));
		//major
		in.readUnsignedShort();
		in.readUnsignedShort();

		constPool = new ConstPool(in);

		accessFlags = in.readUnsignedShort();
		thisClass = in.readUnsignedShort();
		constPool.setThisClassInfo(thisClass);
		superClass = in.readUnsignedShort();

		className = constPool.getClassInfo(thisClass);
	}

	public String getName() {
		return className;
	}
}
