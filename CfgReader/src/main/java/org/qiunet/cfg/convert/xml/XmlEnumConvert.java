package org.qiunet.cfg.convert.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.qiunet.utils.string.StringUtil;

/***
 *
 * @author qiunet
 * 2020-02-08 10:30
 **/
public class XmlEnumConvert implements Converter {
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String value = reader.getValue();
		if (StringUtil.isEmpty(value)) {
			return null;
		}

		Class type = context.getRequiredType();
		if (type.getSuperclass() != Enum.class) {
			type = type.getSuperclass();
		}

		return Enum.valueOf(type, value);
	}

	@Override
	public boolean canConvert(Class type) {
		return type.isEnum() || Enum.class.isAssignableFrom(type);
	}
}
