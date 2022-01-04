package org.qiunet.utils.test.fakeenum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.fakeenum.EnumClass;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.List;

/***
 * 假枚举测试
 *
 * @author qiunet
 * 2022/1/3 14:34
 */
@EnumClass
public class TestFakeEnum {
	private static MyFakeEnum DATA1;
	private static MyFakeEnum DATA2;

	@Test
	public void test() {
		ClassScanner.getInstance(ScannerType.FAKE_ENUM).scanner();

		Assertions.assertEquals("DATA1", DATA1.name());
		Assertions.assertEquals("DATA2", DATA2.name());

		List<MyFakeEnum> values = MyFakeEnum.values(MyFakeEnum.class);
		Assertions.assertEquals(values.size(), 2);
		Assertions.assertThrows(UnsupportedOperationException.class, () -> values.add(new MyFakeEnum()));

		MyFakeEnum data1 = MyFakeEnum.valueOf(MyFakeEnum.class, "DATA1");
		Assertions.assertEquals(data1, DATA1);
	}
}
