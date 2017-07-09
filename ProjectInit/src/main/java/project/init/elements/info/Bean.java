package project.init.elements.info;

/**
 * @author qiunet
 *         Created on 17/2/16 18:01.
 */
public class Bean {
	private String id;
	private String clazz;

	public String getName() {
		return clazz.substring(clazz.lastIndexOf('.')+1, clazz.length());
	}
	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
