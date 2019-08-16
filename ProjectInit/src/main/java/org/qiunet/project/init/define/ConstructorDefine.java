package org.qiunet.project.init.define;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/***
 *
 *
 * qiunet
 * 2019-08-14 20:52
 ***/
public class ConstructorDefine {
	/***
	 * 构造需要的字段
	 */
	private List<String> fields = new ArrayList<>();

	private IEntityDefine entityDefine;

	public void addField(ConstructorArgDefine define) {
		this.fields.add(define.getField());
	}

	void init(IEntityDefine entityDefine) {
		this.entityDefine = entityDefine;
	}

	public List<FieldDefine> getFields() {
		Map<String, FieldDefine> map = entityDefine.getFieldDefines().stream()
			.collect(Collectors.toMap(FieldDefine::getName, field -> field));

		return fields.stream().map(map::get).collect(Collectors.toList());
	}
}
