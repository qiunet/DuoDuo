package org.qiunet.function.rank;

import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.json.TypeReferences;
import org.qiunet.utils.string.StringUtil;

import java.util.Map;

/***
 * 业务会有张表.
 * 保存所有的排行榜数据. 推荐 type -> json(list(rankVo))
 *
 * @author qiunet
 * 2020-11-24 10:08
 */
public class RankVo {
	/**
	 * 排名的对象主键
	 * 玩家id  公会id等
	 */
	private long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 名词
	 */
	transient int rank;
	/**
	 * 值
	 */
	private long value;
	/**
	 * 时间
	 *
	 */
	private long dt;
	/**
	 * 其它信息
	 */
	private String extraInfo;
	private transient Map<String, String> extraInfoMap;

	static RankVo valueOf(long id, String name, long value, Map<String, String> extraInfoMap) {
		RankVo rankVo = new RankVo();
		if (extraInfoMap != null) {
			rankVo.extraInfo = JsonUtil.toJsonString(extraInfoMap);
		}
		rankVo.dt = System.currentTimeMillis();
		rankVo.extraInfoMap = extraInfoMap;
		rankVo.value = value;
		rankVo.name = name;
		rankVo.id = id;
		return rankVo;
	}

	void updateValue(long value) {
		this.dt = System.currentTimeMillis();
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int gotRank() {
		return rank;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public long getDt() {
		return dt;
	}

	public void setDt(long dt) {
		this.dt = dt;
	}

	/**
	 * 获得额外信息里面的参数.
	 * @param key
	 * @return
	 */
	public String getExtraInfo(String key) {
		if (extraInfoMap == null) {
			if (StringUtil.isEmpty(extraInfo)) {
				return null;
			}
			extraInfoMap = JsonUtil.getGeneralObject(extraInfo, TypeReferences.STRING_STRING_MAP);
		}
		return extraInfoMap.get(key);
	}
}
