package org.qiunet.entity2table;

import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Created by zhengj
 * Date: 2019/4/22.
 * Time: 22:42.
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "item", comment = "道具表") @Alias("ItemPo")
public class ItemPo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(isUnsigned = true, isKey = true, isNull = false, comment = "字典表ID")
	private Long id;

	@Column(isUnsigned = true, comment = "父级ID")
	private Long parentId;

	@Column(isUnique = true, comment = "树路径")
	private String path;

	@Column(comment = "字典名称")
	private String dictName;

	@Column(length = 20, comment = "字典编码")
	private String code;

	@Column(length = 5, comment = "排序号")
	private Integer	orderNo;

	@Column(comment = "有子节点，0否，1是")
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
