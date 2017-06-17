package org.qiunet.data.core.support.update;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by qiunet.
 * 17/6/17
 */
public class UpdateFields {
	private Set<String> set;
	private UpdateFields() {
		set = new HashSet<>();
	}

	public static UpdateFields newBuild() {
		return new UpdateFields();
	}

	public UpdateFields append(String fieldName) {
		this.set.add(fieldName);
		return this;
	}

	public void remove(String fieldName) {
		this.set.remove(fieldName);
	}

	public String[] toArray(){
		return set.toArray(new String[set.size()]);
	}
}
