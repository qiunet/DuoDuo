package org.qiunet.utils.classLoader;

import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestHotSwap {
	@Test
	public void testHotSwap() throws URISyntaxException {
		Path path = Paths.get(getClass().getResource("/").toURI());
		ChangeClass changeClass = new ChangeClass();
		changeClass.show();

		ClassHotSwap.hotSwap(path);
		changeClass.show();
		//
		new ChangeClass().show();
	}
}
