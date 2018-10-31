package org.qiunet.data.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/10/31 10:25
 **/
public class TestDataUtil {

	@Test
	public void testGetMap() {
		String [] fields = new String[]{"uid" , "nick" , "regDt", "level" , "exp"};
		TestPo testPo = new TestPo();
		testPo.setExp(11);
		testPo.setLevel(22);
		testPo.setUid(33);
		testPo.setNick("qiunet");
		testPo.setRegDt(new Date());
		Map<String, String > ret = DataUtil.getMap(testPo, fields);
		Assert.assertEquals("qiunet", ret.get("nick"));

		TestPo testPo1 = new TestPo();
		DataUtil.getObjFromMap(ret, testPo1);
		Assert.assertEquals("qiunet" , testPo1.getNick());
	}
}
