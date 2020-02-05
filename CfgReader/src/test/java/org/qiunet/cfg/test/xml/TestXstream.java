package org.qiunet.cfg.test.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.cfg.convert.CfgFieldObjConvertManager;
import org.qiunet.cfg.test.InitCfg;
import org.qiunet.utils.classScanner.ClassScanner;

import java.io.InputStream;
import java.util.Collection;

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

		xStream.alias("configs", Configs.class);
		xStream.addImplicitArray(Configs.class, "config", InitCfg.class);

		CfgFieldObjConvertManager.getInstance().getConverts().forEach(convert -> xStream.registerConverter(convert, XStream.PRIORITY_VERY_HIGH - 1));

		Object obj = xStream.fromXML(stream);
		Assert.assertSame(obj.getClass(), Configs.class);
		Assert.assertEquals(((Configs) obj).getConfig().size(), 3);
	}
}
