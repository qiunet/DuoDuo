package org.qiunet.function.rank;

import com.google.common.collect.Maps;
import org.qiunet.data.core.support.db.event.DbLoaderOverEventData;
import org.qiunet.listener.event.EventHandlerWeightType;
import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/***
 *
 *
 * @author qiunet
 * 2020-11-24 17:15
 */
enum RankHandlerManager0 implements IApplicationContextAware {
	instances;
	private IApplicationContext context;
	private Map<IRankType, IRankHandler> map = Maps.newHashMap();

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.context = context;
	}

	@EventListener(EventHandlerWeightType.LESS)
	private void onDbLoadOver(DbLoaderOverEventData eventData){
		Set<Class<? extends IRankHandler>> classes = context.getSubTypesOf(IRankHandler.class);
		for (Class<? extends IRankHandler> aClass : classes) {
			if (Modifier.isAbstract(aClass.getModifiers())) {
				continue;
			}
			IRankHandler obj = (IRankHandler) context.getInstanceOfClass(aClass);
			map.put((IRankType) obj.getType(), obj);
		}
	}

	/**
	 * 获取指定类型的rankHandler
	 * @param rankType 类型
	 * @return
	 */
	<Type extends Enum<Type> & IRankType, Handler extends IRankHandler<Type>>
	Handler getRankHandler(Type rankType) {
		return (Handler) map.get(rankType);
	}
}
