package org.qiunet.data.model2table;

import org.qiunet.data.model2table.annotation.Column;
import org.qiunet.data.model2table.annotation.Table;
import org.qiunet.data.model2table.constants.MySqlTypeConstant;

import java.io.Serializable;

/**
 * Created by zhengj
 * Date: 2019/4/22.
 * Time: 22:42.
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "sys_dict", comment = "字典表")
public class SysDict implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id", isUnsigned = true, type = MySqlTypeConstant.BIGINT, length = 64, isKey = true, isNull = false, comment = "字典表ID")
	private Long id;

	@Column(name = "parentId", isUnsigned = true, type = MySqlTypeConstant.BIGINT, length = 64, isNull = true, comment = "父级ID")
	private Long parentId;

	@Column(name = "path", type = MySqlTypeConstant.VARCHAR, length = 100, isUnique = true, comment = "树路径")
	private String path;

	@Column(name = "dictName", type = MySqlTypeConstant.VARCHAR, length = 100, comment = "字典名称")
	private String dictName;

	@Column(name = "code", type = MySqlTypeConstant.VARCHAR, length = 20, comment = "字典编码")
	private String code;

	@Column(name = "orderNo", type = MySqlTypeConstant.INT, length = 5, comment = "排序号")
	private Integer	orderNo;

	@Column(name = "hasChildren", type = MySqlTypeConstant.TINYINT, length = 1, comment = "有子节点，0否，1是")
	private boolean	hasChildren;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
}