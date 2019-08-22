package org.qiunet.entity2table;

import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;

/**
 * Created by zhengj
 * Date: 2019/7/26.
 * Time: 15:41.
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "sys_dict", comment = "字典表")
public class SysDict {

	@Column(isKey = true, isNull = false, comment = "字典表ID")
	private Long id;

	@Column(comment = "父级ID")
	private Long parentId;

	@Column(comment = "树路径")
	private String path;

	@Column(comment = "字典名称")
	private String dictName;

	@Column(comment = "字典编码")
	private String code;

	@Column(comment = "排序号")
	private Integer	orderNo;

	@Column(comment = "排序号1")
	private Integer	orderNo1;

	@Column(comment = "排序号2")
	private long orderNo2;


	public Integer getOrderNo1() {
		return orderNo1;
	}

	public void setOrderNo1(Integer orderNo1) {
		this.orderNo1 = orderNo1;
	}

	public long getOrderNo2() {
		return orderNo2;
	}

	public void setOrderNo2(long orderNo2) {
		this.orderNo2 = orderNo2;
	}

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
}
