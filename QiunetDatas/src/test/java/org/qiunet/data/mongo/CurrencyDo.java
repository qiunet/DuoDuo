package org.qiunet.data.mongo;

import java.util.HashMap;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2024/2/22 15:54
 ***/
public class CurrencyDo extends BasicMongoEntity<Long> {

	private Map<CurrencyType, Long> purse;

	private String name;

	public CurrencyDo() {
	}

	public CurrencyDo(Long id) {
		super(id);
	}

	public static CurrencyDo find(long id) {
		return IMongoEntity._find(CurrencyDo.class, id);
	}

	public static CurrencyDo valueOf(long playerId, String name, long m1, long m2){
		CurrencyDo data = new CurrencyDo(playerId);
		data.purse = new HashMap<>();
		data.purse.put(CurrencyType.M1, m1);
		data.purse.put(CurrencyType.M2, m2);
		data.name = name;
		return data;
	}

	public long getPurse(CurrencyType type) {
		return purse.getOrDefault(type, 0L);
	}

	public String getName() {
		return name;
	}
}
