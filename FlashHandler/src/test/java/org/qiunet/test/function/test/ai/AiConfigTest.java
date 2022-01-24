package org.qiunet.test.function.test.ai;

import org.junit.jupiter.api.Test;
import org.qiunet.function.condition.doc.CreateAiConfig;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 * @author qiunet
 * 2022/1/24 16:45
 */
public class AiConfigTest {

	@Test
	public void test() throws Exception {
		ClassScanner.getInstance(ScannerType.CREATE_AI_CONFIG).scanner();
		CreateAiConfig.generator("/Users/qiunet/Desktop");
	}
}
