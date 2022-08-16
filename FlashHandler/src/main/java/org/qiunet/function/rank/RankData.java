package org.qiunet.function.rank;

import com.alibaba.fastjson.annotation.JSONField;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.exceptions.CustomException;

import java.util.HashMap;
import java.util.Map;

/***
 * 业务会有张表.
 * 保存所有的排行榜数据. 推荐 type → json(list(rankVo))
 *
 * @author qiunet
 * 2020-11-24 10:08
 */
public class RankData extends HashMap<String, Object> implements IKeyValueData<String, Object> {
	/*** 值*/
	private static final String VALUE = "_value";
	/**名字*/
	private static final String NAME = "_name";
	/*** 排名的对象主键* 玩家id  公会id等*/
	private static final String ID = "_id";
	/**时间*/
	private static final String DT = "_dt";
	/**
	 * 名词
	 */
	@JSONField(serialize = false, deserialize = false)
	transient int rank;

	/**
	 * 默认构造
	 */
	public RankData(){}

	/**
	 * 构造一个builder
	 * @param id 排行榜的id
	 * @param value 排行榜的值
	 * @return  RankDataBuilder
	 */
	public static RankDataBuilder custom(long id, long value) {
		return new RankDataBuilder(id, value);
	}

	static RankData valueOf(long id, long value) {
		RankData rankData = new RankData();
		rankData.updateValue(value);
		rankData.put(ID, id);
		return rankData;
	}

	public int gotRank() {
		return rank;
	}

	public long getId() {
		return getLong(ID);
	}

	public long getDt() {
		return getLong(DT);
	}


	public long getValue() {
		return getLong(VALUE);
	}

	public String getName() {
		return getString(NAME, "unknown");
	}

	void updateValue(long value) {
		this.put(DT, System.currentTimeMillis());
		this.put(VALUE, value);
	}

	@Override
	public Map<String, Object> returnMap() {
		return this;
	}



	/***
	 * 构造一个RankData
	 *
	 * @author qiunet
	 * 2021/12/17 15:32
	 */
	public static class RankDataBuilder {
		private final RankData rankData;
		private RankDataBuilder(long id, long value) {
			this.rankData = RankData.valueOf(id, value);
		}
		/**
		 * 添加名字
		 * @param name 添加name
		 * @return 实例本身
		 */
		public RankDataBuilder addName(String name) {
			this.rankData.put(NAME, name);
			return this;
		}
		/**
		 * 添加一些字段.
		 * @param key 增加key
		 * @param val 增加值
		 * @return 返回builder本实例
		 */
		public RankDataBuilder addData(String key, Object val) {
			if (CommonUtil.existInList(key, DT, VALUE, NAME, ID)) {
				throw new CustomException("{} is key word!", key);
			}
			this.rankData.put(key, val);
			return this;
		}

		public RankData build(){
			return rankData;
		}
	}
}
