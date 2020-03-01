package org.qiunet.cfg.test.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.cfg.convert.CfgFieldObjConvertManager;
import org.qiunet.cfg.test.InitCfg;
import org.qiunet.utils.classScanner.ClassScanner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/***
 *
 * @author qiunet
 * 2020-02-04 22:20
 **/
public class TestXstream {
	@Test
	public void testXstreamReader(){
		ClassScanner.getInstance().scanner();
		InputStream stream = getClass().getClassLoader().getResourceAsStream("config/init/init_data.xml");
		XStream xStream = new XStream();
		// allow some basics
		xStream.addPermission(NullPermission.NULL);
		xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xStream.allowTypeHierarchy(Collection.class);

		xStream.alias("configs", ArrayList.class);
		xStream.alias("config", InitCfg.class);

		CfgFieldObjConvertManager.getInstance().getConverts().forEach(convert -> xStream.registerConverter(convert, XStream.PRIORITY_VERY_HIGH - 1));

		List<InitCfg> obj = (List<InitCfg>) xStream.fromXML(stream);
		Assert.assertEquals(obj.size(), 3);
	}
}
