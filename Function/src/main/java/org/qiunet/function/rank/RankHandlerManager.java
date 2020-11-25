package org.qiunet.function.rank;

/***
 *
 *
 * @author qiunet
 * 2020-11-25 15:01
 */
public class RankHandlerManager {
	/**
	 * 取得对应的handler
	 * @param rankType
	 * @param <Type>
	 * @param <Handler>
	 * @return
	 */
	public static <Type extends Enum<Type> & IRankType, Handler extends IRankHandler<Type>>
	Handler getRankHandler(Type rankType) {
		return RankHandlerManager0.instances.getRankHandler(rankType);
	}
}
