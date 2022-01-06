package org.qAgent;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Constant pool table.
 */
final class ConstPool
{
	LongVector items;
	int numOfItems;
	int thisClassInfo;
	Map<ConstInfo,ConstInfo> itemsCache;

	/**
	 * Constructs a constant pool table from the given byte stream.
	 *
	 * @param in        byte stream.
	 */
	public ConstPool(DataInputStream in) throws IOException
	{
		itemsCache = null;
		thisClassInfo = 0;
		/* read() initializes items and numOfItems, and do addItem(null).
		 */
		read(in);
	}

	void setThisClassInfo(int i)
	{
		thisClassInfo = i;
	}

	ConstInfo getItem(int n)
	{
		return items.elementAt(n);
	}

	/**
	 * Reads <code>CONSTANT_Class_info</code> structure
	 * at the given index.
	 *
	 * @return  a fully-qualified class or interface name specified
	 *          by <code>name_index</code>.  If the type is an array
	 *          type, this method returns an encoded name like
	 *          <code>[Ljava.lang.Object;</code> (note that the separators
	 *          are not slashes but dots).
	 */
	public String getClassInfo(int index)
	{
		ClassInfo c = (ClassInfo)getItem(index);
		if (c == null)
			return null;
		return getUtf8Info(c.name).replace('/', '.');

	}

	/**
	 * Reads <code>CONSTANT_utf8_info</code> structure
	 * at the given index.
	 *
	 * @return the string specified by this entry.
	 */
	public String getUtf8Info(int index)
	{
		Utf8Info utf = (Utf8Info)getItem(index);
		return utf.string;
	}



	private int addItem0(ConstInfo info)
	{
		items.addElement(info);
		return numOfItems++;
	}

	private int addItem(ConstInfo info)
	{
		if (itemsCache == null)
			itemsCache = makeItemsCache(items);

		ConstInfo found = itemsCache.get(info);
		if (found != null)
			return found.index;
		items.addElement(info);
		itemsCache.put(info, info);
		return numOfItems++;
	}

	/**
	 * Copies the n-th item in this ConstPool object into the destination
	 * ConstPool object.
	 * The class names that the item refers to are renamed according
	 * to the given map.
	 *
	 * @param n                 the <i>n</i>-th item
	 * @param dest              destination constant pool table
	 * @param classnames        the map or null.
	 * @return the index of the copied item into the destination ClassPool.
	 */
	public int copy(int n, ConstPool dest, Map<String,String> classnames)
	{
		if (n == 0)
			return 0;

		ConstInfo info = getItem(n);
		return info.copy(this, dest, classnames);
	}

	int addConstInfoPadding() {
		return addItem0(new ConstInfoPadding(numOfItems));
	}


	/**
	 * Adds a new <code>CONSTANT_Class_info</code> structure.
	 *
	 * <p>This also adds a <code>CONSTANT_Utf8_info</code> structure
	 * for storing the class name.
	 *
	 * @param qname     a fully-qualified class name
	 *                  (or the JVM-internal representation of that name).
	 * @return          the index of the added entry.
	 */
	public int addClassInfo(String qname)
	{
		int utf8 = addUtf8Info(qname.replace('.', '/'));
		return addItem(new ClassInfo(utf8, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_NameAndType_info</code> structure.
	 *
	 * <p>This also adds <code>CONSTANT_Utf8_info</code> structures.
	 *
	 * @param name      <code>name_index</code>
	 * @param type      <code>descriptor_index</code>
	 * @return          the index of the added entry.
	 */
	public int addNameAndTypeInfo(String name, String type)
	{
		return addNameAndTypeInfo(addUtf8Info(name), addUtf8Info(type));
	}

	/**
	 * Adds a new <code>CONSTANT_NameAndType_info</code> structure.
	 *
	 * @param name      <code>name_index</code>
	 * @param type      <code>descriptor_index</code>
	 * @return          the index of the added entry.
	 */
	public int addNameAndTypeInfo(int name, int type)
	{
		return addItem(new NameAndTypeInfo(name, type, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_Fieldref_info</code> structure.
	 *
	 * <p>This also adds a new <code>CONSTANT_NameAndType_info</code>
	 * structure.
	 *
	 * @param classInfo         <code>class_index</code>
	 * @param name              <code>name_index</code>
	 *                          of <code>CONSTANT_NameAndType_info</code>.
	 * @param type              <code>descriptor_index</code>
	 *                          of <code>CONSTANT_NameAndType_info</code>.
	 * @return          the index of the added entry.
	 */
	public int addFieldrefInfo(int classInfo, String name, String type)
	{
		int nt = addNameAndTypeInfo(name, type);
		return addFieldrefInfo(classInfo, nt);
	}

	/**
	 * Adds a new <code>CONSTANT_Fieldref_info</code> structure.
	 *
	 * @param classInfo         <code>class_index</code>
	 * @param nameAndTypeInfo   <code>name_and_type_index</code>.
	 * @return          the index of the added entry.
	 */
	public int addFieldrefInfo(int classInfo, int nameAndTypeInfo)
	{
		return addItem(new FieldrefInfo(classInfo, nameAndTypeInfo,
				numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_Methodref_info</code> structure.
	 *
	 * <p>This also adds a new <code>CONSTANT_NameAndType_info</code>
	 * structure.
	 *
	 * @param classInfo         <code>class_index</code>
	 * @param name              <code>name_index</code>
	 *                          of <code>CONSTANT_NameAndType_info</code>.
	 * @param type              <code>descriptor_index</code>
	 *                          of <code>CONSTANT_NameAndType_info</code>.
	 * @return          the index of the added entry.
	 */
	public int addMethodrefInfo(int classInfo, String name, String type)
	{
		int nt = addNameAndTypeInfo(name, type);
		return addMethodrefInfo(classInfo, nt);
	}

	/**
	 * Adds a new <code>CONSTANT_Methodref_info</code> structure.
	 *
	 * @param classInfo         <code>class_index</code>
	 * @param nameAndTypeInfo   <code>name_and_type_index</code>.
	 * @return          the index of the added entry.
	 */
	public int addMethodrefInfo(int classInfo, int nameAndTypeInfo)
	{
		return addItem(new MethodrefInfo(classInfo,
				nameAndTypeInfo, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_InterfaceMethodref_info</code>
	 * structure.
	 *
	 * <p>This also adds a new <code>CONSTANT_NameAndType_info</code>
	 * structure.
	 *
	 * @param classInfo         <code>class_index</code>
	 * @param name              <code>name_index</code>
	 *                          of <code>CONSTANT_NameAndType_info</code>.
	 * @param type              <code>descriptor_index</code>
	 *                          of <code>CONSTANT_NameAndType_info</code>.
	 * @return          the index of the added entry.
	 */
	public int addInterfaceMethodrefInfo(int classInfo,
										 String name,
										 String type)
	{
		int nt = addNameAndTypeInfo(name, type);
		return addInterfaceMethodrefInfo(classInfo, nt);
	}

	/**
	 * Adds a new <code>CONSTANT_InterfaceMethodref_info</code>
	 * structure.
	 *
	 * @param classInfo         <code>class_index</code>
	 * @param nameAndTypeInfo   <code>name_and_type_index</code>.
	 * @return          the index of the added entry.
	 */
	public int addInterfaceMethodrefInfo(int classInfo,
										 int nameAndTypeInfo)
	{
		return addItem(new InterfaceMethodrefInfo(classInfo,
				nameAndTypeInfo,
				numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_String_info</code>
	 * structure.
	 *
	 * <p>This also adds a new <code>CONSTANT_Utf8_info</code>
	 * structure.
	 *
	 * @return          the index of the added entry.
	 */
	public int addStringInfo(String str)
	{
		int utf = addUtf8Info(str);
		return addItem(new StringInfo(utf, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_Integer_info</code>
	 * structure.
	 *
	 * @return          the index of the added entry.
	 */
	public int addIntegerInfo(int i)
	{
		return addItem(new IntegerInfo(i, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_Float_info</code>
	 * structure.
	 *
	 * @return          the index of the added entry.
	 */
	public int addFloatInfo(float f)
	{
		return addItem(new FloatInfo(f, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_Long_info</code>
	 * structure.
	 *
	 * @return          the index of the added entry.
	 */
	public int addLongInfo(long l)
	{
		int i = addItem(new LongInfo(l, numOfItems));
		if (i == numOfItems - 1)    // if not existing
			addConstInfoPadding();

		return i;
	}

	/**
	 * Adds a new <code>CONSTANT_Double_info</code>
	 * structure.
	 *
	 * @return          the index of the added entry.
	 */
	public int addDoubleInfo(double d)
	{
		int i = addItem(new DoubleInfo(d, numOfItems));
		if (i == numOfItems - 1)    // if not existing
			addConstInfoPadding();

		return i;
	}

	/**
	 * Adds a new <code>CONSTANT_Utf8_info</code>
	 * structure.
	 *
	 * @return          the index of the added entry.
	 */
	public int addUtf8Info(String utf8)
	{
		return addItem(new Utf8Info(utf8, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_MethodHandle_info</code>
	 * structure.
	 *
	 * @param kind      <code>reference_kind</code>
	 * @param index     <code>reference_index</code>.
	 * @return          the index of the added entry.
	 *
	 * @since 3.17
	 */
	public int addMethodHandleInfo(int kind, int index)
	{
		return addItem(new MethodHandleInfo(kind, index, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_MethodType_info</code>
	 * structure.
	 *
	 * @param desc      <code>descriptor_index</code>.
	 * @return          the index of the added entry.
	 *
	 * @since 3.17
	 */
	public int addMethodTypeInfo(int desc)
	{
		return addItem(new MethodTypeInfo(desc, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_InvokeDynamic_info</code>
	 * structure.
	 *
	 * @param bootstrap     <code>bootstrap_method_attr_index</code>.
	 * @param nameAndType   <code>name_and_type_index</code>.
	 * @return          the index of the added entry.
	 *
	 * @since 3.17
	 */
	public int addInvokeDynamicInfo(int bootstrap, int nameAndType)
	{
		return addItem(new InvokeDynamicInfo(bootstrap, nameAndType, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_Dynamic_info</code> structure.
	 *
	 * @param bootstrap   <code>bootstrap_method_attr_index</code>.
	 * @param nameAndType <code>name_and_type_index</code>.
	 * @return the index of the added entry.
	 * @since 3.26
	 */
	public int addDynamicInfo(int bootstrap, int nameAndType) {
		return addItem(new DynamicInfo(bootstrap, nameAndType, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_Module_info</code>
	 * @param nameIndex         the index of the Utf8 entry.
	 * @return          the index of the added entry.
	 * @since 3.22
	 */
	public int addModuleInfo(int nameIndex)
	{
		return addItem(new ModuleInfo(nameIndex, numOfItems));
	}

	/**
	 * Adds a new <code>CONSTANT_Package_info</code>
	 * @param nameIndex         the index of the Utf8 entry.
	 * @return          the index of the added entry.
	 * @since 3.22
	 */
	public int addPackageInfo(int nameIndex)
	{
		return addItem(new PackageInfo(nameIndex, numOfItems));
	}



	private void read(DataInputStream in) throws IOException
	{
		int n = in.readUnsignedShort();

		items = new LongVector(n);
		numOfItems = 0;
		addItem0(null);          // index 0 is reserved by the JVM.

		while (--n > 0) {       // index 0 is reserved by JVM
			int tag = readOne(in);
			if ((tag == LongInfo.tag) || (tag == DoubleInfo.tag)) {
				addConstInfoPadding();
				--n;
			}
		}
	}

	private static Map<ConstInfo,ConstInfo> makeItemsCache(LongVector items)
	{
		Map<ConstInfo,ConstInfo> cache = new HashMap<ConstInfo,ConstInfo>();
		int i = 1;
		while (true) {
			ConstInfo info = items.elementAt(i++);
			if (info == null)
				break;
			cache.put(info, info);
		}

		return cache;
	}

	private int readOne(DataInputStream in) throws IOException
	{
		ConstInfo info;
		int tag = in.readUnsignedByte();
		switch (tag) {
			case Utf8Info.tag :                     // 1
				info = new Utf8Info(in, numOfItems);
				break;
			case IntegerInfo.tag :                  // 3
				info = new IntegerInfo(in, numOfItems);
				break;
			case FloatInfo.tag :                    // 4
				info = new FloatInfo(in, numOfItems);
				break;
			case LongInfo.tag :                     // 5
				info = new LongInfo(in, numOfItems);
				break;
			case DoubleInfo.tag :                   // 6
				info = new DoubleInfo(in, numOfItems);
				break;
			case ClassInfo.tag :                    // 7
				info = new ClassInfo(in, numOfItems);
				break;
			case StringInfo.tag :                   // 8
				info = new StringInfo(in, numOfItems);
				break;
			case FieldrefInfo.tag :                 // 9
				info = new FieldrefInfo(in, numOfItems);
				break;
			case MethodrefInfo.tag :                // 10
				info = new MethodrefInfo(in, numOfItems);
				break;
			case InterfaceMethodrefInfo.tag :       // 11
				info = new InterfaceMethodrefInfo(in, numOfItems);
				break;
			case NameAndTypeInfo.tag :              // 12
				info = new NameAndTypeInfo(in, numOfItems);
				break;
			case MethodHandleInfo.tag :             // 15
				info = new MethodHandleInfo(in, numOfItems);
				break;
			case MethodTypeInfo.tag :               // 16
				info = new MethodTypeInfo(in, numOfItems);
				break;
			case DynamicInfo.tag :                  // 17
				info = new DynamicInfo(in, numOfItems);
				break;
			case InvokeDynamicInfo.tag :            // 18
				info = new InvokeDynamicInfo(in, numOfItems);
				break;
			case ModuleInfo.tag :                   // 19
				info = new ModuleInfo(in, numOfItems);
				break;
			case PackageInfo.tag :                  // 20
				info = new PackageInfo(in, numOfItems);
				break;
			default :
				throw new IOException("invalid constant type: "
						+ tag + " at " + numOfItems);
		}

		addItem0(info);
		return tag;
	}
}

abstract class ConstInfo
{
	int index;

	public ConstInfo(int i) { index = i; }

	public abstract int copy(ConstPool src, ConstPool dest,
							 Map<String, String> classnames);
	// ** classnames is a mapping between JVM names.

}

/* padding following DoubleInfo or LongInfo.
 */
class ConstInfoPadding extends ConstInfo
{
	public ConstInfoPadding(int i) { super(i); }

	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String,String> map)
	{
		return dest.addConstInfoPadding();
	}
}

class ClassInfo extends ConstInfo
{
	static final int tag = 7;
	int name;

	public ClassInfo(int className, int index)
	{
		super(index);
		name = className;
	}

	public ClassInfo(DataInputStream in, int index) throws IOException
	{
		super(index);
		name = in.readUnsignedShort();
	}

	@Override
	public int hashCode() { return name; }

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof ClassInfo && ((ClassInfo)obj).name == name;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String,String> map)
	{
		String classname = src.getUtf8Info(name);
		if (map != null) {
			String newname = map.get(classname);
			if (newname != null)
				classname = newname;
		}

		return dest.addClassInfo(classname);
	}
}

class NameAndTypeInfo extends ConstInfo
{
	static final int tag = 12;
	int memberName;
	int typeDescriptor;

	public NameAndTypeInfo(int name, int type, int index)
	{
		super(index);
		memberName = name;
		typeDescriptor = type;
	}

	public NameAndTypeInfo(DataInputStream in, int index) throws IOException
	{
		super(index);
		memberName = in.readUnsignedShort();
		typeDescriptor = in.readUnsignedShort();
	}

	@Override
	public int hashCode() { return (memberName << 16) ^ typeDescriptor; }

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof NameAndTypeInfo) {
			NameAndTypeInfo nti = (NameAndTypeInfo)obj;
			return nti.memberName == memberName
					&& nti.typeDescriptor == typeDescriptor;
		}
		return false;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String, String> classnames) {
		return 0;
	}

}

abstract class MemberrefInfo extends ConstInfo
{
	int classIndex;
	int nameAndTypeIndex;

	public MemberrefInfo(int cindex, int ntindex, int thisIndex)
	{
		super(thisIndex);
		classIndex = cindex;
		nameAndTypeIndex = ntindex;
	}

	public MemberrefInfo(DataInputStream in, int thisIndex)
			throws IOException
	{
		super(thisIndex);
		classIndex = in.readUnsignedShort();
		nameAndTypeIndex = in.readUnsignedShort();
	}

	@Override
	public int hashCode() { return (classIndex << 16) ^ nameAndTypeIndex; }

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MemberrefInfo) {
			MemberrefInfo mri = (MemberrefInfo)obj;
			return mri.classIndex == classIndex
					&& mri.nameAndTypeIndex == nameAndTypeIndex
					&& mri.getClass() == this.getClass();
		}
		return false;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String,String> map)
	{
		int classIndex2 = src.getItem(classIndex).copy(src, dest, map);
		int ntIndex2 = src.getItem(nameAndTypeIndex).copy(src, dest, map);
		return copy2(dest, classIndex2, ntIndex2);
	}

	abstract protected int copy2(ConstPool dest, int cindex, int ntindex);
}

class FieldrefInfo extends MemberrefInfo
{
	static final int tag = 9;

	public FieldrefInfo(int cindex, int ntindex, int thisIndex)
	{
		super(cindex, ntindex, thisIndex);
	}

	public FieldrefInfo(DataInputStream in, int thisIndex)
			throws IOException
	{
		super(in, thisIndex);
	}

	@Override
	protected int copy2(ConstPool dest, int cindex, int ntindex)
	{
		return dest.addFieldrefInfo(cindex, ntindex);
	}
}

class MethodrefInfo extends MemberrefInfo
{
	static final int tag = 10;

	public MethodrefInfo(int cindex, int ntindex, int thisIndex)
	{
		super(cindex, ntindex, thisIndex);
	}

	public MethodrefInfo(DataInputStream in, int thisIndex)
			throws IOException
	{
		super(in, thisIndex);
	}

	@Override
	protected int copy2(ConstPool dest, int cindex, int ntindex)
	{
		return dest.addMethodrefInfo(cindex, ntindex);
	}
}

class InterfaceMethodrefInfo extends MemberrefInfo
{
	static final int tag = 11;

	public InterfaceMethodrefInfo(int cindex, int ntindex, int thisIndex)
	{
		super(cindex, ntindex, thisIndex);
	}

	public InterfaceMethodrefInfo(DataInputStream in, int thisIndex)
			throws IOException
	{
		super(in, thisIndex);
	}

	@Override
	protected int copy2(ConstPool dest, int cindex, int ntindex)
	{
		return dest.addInterfaceMethodrefInfo(cindex, ntindex);
	}
}

class StringInfo extends ConstInfo
{
	static final int tag = 8;
	int string;

	public StringInfo(int str, int index)
	{
		super(index);
		string = str;
	}

	public StringInfo(DataInputStream in, int index) throws IOException
	{
		super(index);
		string = in.readUnsignedShort();
	}

	@Override
	public int hashCode() { return string; }

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof StringInfo && ((StringInfo)obj).string == string;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String,String> map)
	{
		return dest.addStringInfo(src.getUtf8Info(string));
	}
}

class IntegerInfo extends ConstInfo
{
	static final int tag = 3;
	int value;

	public IntegerInfo(int v, int index)
	{
		super(index);
		value = v;
	}

	public IntegerInfo(DataInputStream in, int index) throws IOException
	{
		super(index);
		value = in.readInt();
	}

	@Override
	public int hashCode() { return value; }

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof IntegerInfo && ((IntegerInfo)obj).value == value;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String,String> map)
	{
		return dest.addIntegerInfo(value);
	}
}

class FloatInfo extends ConstInfo
{
	static final int tag = 4;
	float value;

	public FloatInfo(float f, int index)
	{
		super(index);
		value = f;
	}

	public FloatInfo(DataInputStream in, int index) throws IOException
	{
		super(index);
		value = in.readFloat();
	}

	@Override
	public int hashCode() { return Float.floatToIntBits(value); }

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof FloatInfo && ((FloatInfo)obj).value == value;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String,String> map)
	{
		return dest.addFloatInfo(value);
	}

}

class LongInfo extends ConstInfo
{
	static final int tag = 5;
	long value;

	public LongInfo(long l, int index)
	{
		super(index);
		value = l;
	}

	public LongInfo(DataInputStream in, int index) throws IOException
	{
		super(index);
		value = in.readLong();
	}

	@Override
	public int hashCode() { return (int)(value ^ (value >>> 32)); }

	@Override
	public boolean equals(Object obj) {
		return obj instanceof LongInfo && ((LongInfo)obj).value == value;
	}
	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String,String> map)
	{
		return dest.addLongInfo(value);
	}
}

class DoubleInfo extends ConstInfo
{
	static final int tag = 6;
	double value;

	public DoubleInfo(double d, int index)
	{
		super(index);
		value = d;
	}

	public DoubleInfo(DataInputStream in, int index) throws IOException
	{
		super(index);
		value = in.readDouble();
	}

	@Override
	public int hashCode() {
		long v = Double.doubleToLongBits(value);
		return (int)(v ^ (v >>> 32));
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DoubleInfo
				&& ((DoubleInfo)obj).value == value;
	}
	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String,String> map)
	{
		return dest.addDoubleInfo(value);
	}
}

class Utf8Info extends ConstInfo
{
	static final int tag = 1;
	String string;

	public Utf8Info(String utf8, int index)
	{
		super(index);
		string = utf8;
	}

	public Utf8Info(DataInputStream in, int index)
			throws IOException
	{
		super(index);
		string = in.readUTF();
	}

	@Override
	public int hashCode() {
		return string.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Utf8Info
				&& ((Utf8Info)obj).string.equals(string);
	}
	@Override
	public int copy(ConstPool src, ConstPool dest,
					Map<String,String> map)
	{
		return dest.addUtf8Info(string);
	}
}

class MethodHandleInfo extends ConstInfo {
	static final int tag = 15;
	int refKind, refIndex;

	public MethodHandleInfo(int kind, int referenceIndex, int index) {
		super(index);
		refKind = kind;
		refIndex = referenceIndex;
	}

	public MethodHandleInfo(DataInputStream in, int index)
			throws IOException
	{
		super(index);
		refKind = in.readUnsignedByte();
		refIndex = in.readUnsignedShort();
	}

	@Override
	public int hashCode() { return (refKind << 16) ^ refIndex; }

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof MethodHandleInfo) {
			MethodHandleInfo mh = (MethodHandleInfo)obj;
			return mh.refKind == refKind && mh.refIndex == refIndex;
		}
		return false;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest,
					Map<String,String> map)
	{
		return dest.addMethodHandleInfo(refKind,
				src.getItem(refIndex).copy(src, dest, map));
	}
}

class MethodTypeInfo extends ConstInfo
{
	static final int tag = 16;
	int descriptor;

	public MethodTypeInfo(int desc, int index)
	{
		super(index);
		descriptor = desc;
	}

	public MethodTypeInfo(DataInputStream in, int index)
			throws IOException
	{
		super(index);
		descriptor = in.readUnsignedShort();
	}

	@Override
	public int hashCode() { return descriptor; }

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof MethodTypeInfo)
			return ((MethodTypeInfo)obj).descriptor == descriptor;
		return false;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String,String> map)
	{
		return 0;
	}
}

class InvokeDynamicInfo extends ConstInfo
{
	static final int tag = 18;
	int bootstrap, nameAndType;

	public InvokeDynamicInfo(int bootstrapMethod,
							 int ntIndex, int index)
	{
		super(index);
		bootstrap = bootstrapMethod;
		nameAndType = ntIndex;
	}

	public InvokeDynamicInfo(DataInputStream in, int index)
			throws IOException
	{
		super(index);
		bootstrap = in.readUnsignedShort();
		nameAndType = in.readUnsignedShort();
	}

	@Override
	public int hashCode() { return (bootstrap << 16) ^ nameAndType; }

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof InvokeDynamicInfo) {
			InvokeDynamicInfo iv = (InvokeDynamicInfo)obj;
			return iv.bootstrap == bootstrap
					&& iv.nameAndType == nameAndType;
		}
		return false;
	}
	@Override
	public int copy(ConstPool src, ConstPool dest,
					Map<String,String> map)
	{
		return dest.addInvokeDynamicInfo(bootstrap,
				src.getItem(nameAndType).copy(src, dest, map));
	}
}

class DynamicInfo extends ConstInfo {

	static final int tag = 17;
	int bootstrap, nameAndType;

	public DynamicInfo(int bootstrapMethod, int ntIndex, int index) {
		super(index);
		bootstrap = bootstrapMethod;
		nameAndType = ntIndex;
	}

	public DynamicInfo(DataInputStream in, int index) throws IOException {
		super(index);
		bootstrap = in.readUnsignedShort();
		nameAndType = in.readUnsignedShort();
	}

	@Override
	public int hashCode() {
		return (bootstrap << 16) ^ nameAndType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DynamicInfo) {
			DynamicInfo iv = (DynamicInfo)obj;
			return iv.bootstrap == bootstrap && iv.nameAndType == nameAndType;
		}
		return false;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest, Map<String, String> map) {
		return dest.addDynamicInfo(bootstrap, src.getItem(nameAndType).copy(src, dest, map));
	}
}

class ModuleInfo extends ConstInfo
{
	static final int tag = 19;
	int name;

	public ModuleInfo(int moduleName, int index)
	{
		super(index);
		name = moduleName;
	}

	public ModuleInfo(DataInputStream in, int index)
			throws IOException
	{
		super(index);
		name = in.readUnsignedShort();
	}

	@Override
	public int hashCode() { return name; }

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof ModuleInfo
				&& ((ModuleInfo)obj).name == name;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest,
					Map<String,String> map)
	{
		String moduleName = src.getUtf8Info(name);
		int newName = dest.addUtf8Info(moduleName);
		return dest.addModuleInfo(newName);
	}
}

class PackageInfo extends ConstInfo
{
	static final int tag = 20;
	int name;

	public PackageInfo(int moduleName, int index)
	{
		super(index);
		name = moduleName;
	}

	public PackageInfo(DataInputStream in, int index)
			throws IOException
	{
		super(index);
		name = in.readUnsignedShort();
	}

	@Override
	public int hashCode() { return name; }

	@Override
	public boolean equals(Object obj) {
		return obj instanceof PackageInfo
				&& ((PackageInfo)obj).name == name;
	}

	@Override
	public int copy(ConstPool src, ConstPool dest,
					Map<String,String> map)
	{
		String packageName = src.getUtf8Info(name);
		int newName = dest.addUtf8Info(packageName);
		return dest.addModuleInfo(newName);
	}
}
