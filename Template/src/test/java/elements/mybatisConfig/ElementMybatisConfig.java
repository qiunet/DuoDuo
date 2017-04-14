package elements.mybatisConfig;

import org.qiunet.template.parse.xml.SubVmElement;

/**
 * @author qiunet
 *         Created on 17/2/17 16:36.
 */
public class ElementMybatisConfig extends SubVmElement {
	
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	protected String getOutFilePath() {
		return path;
	}
}
