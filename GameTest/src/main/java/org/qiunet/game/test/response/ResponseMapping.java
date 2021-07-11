package org.qiunet.game.test.response;

import com.google.common.collect.Maps;
import org.qiunet.game.test.behavior.action.IBehaviorAction;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Method;
import java.util.Map;

/***
 * 测试用例里面response 扫描
 *
 * @author qiunet
 * 2021-07-07 15:15
 */
public class ResponseMapping implements IApplicationContextAware {

	private static final Map<Integer, Method> methodMapping = Maps.newHashMap();

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		context.getMethodsAnnotatedWith(Response.class).forEach(mtd -> {
			Response annotation = mtd.getAnnotation(Response.class);
			if (methodMapping.containsKey(annotation.value())) {
				throw new CustomException("game.test Response id [{}] is repeated!", annotation.value());
			}

			if (! mtd.getDeclaringClass().isAssignableFrom(IBehaviorAction.class)) {
				throw new CustomException("Response [id:{}] need define in IBehaviorAction!", annotation.value());
			}

			methodMapping.put(annotation.value(), mtd);
		});
	}

	/**
	 * 获得responseID对应的方法
	 * @param responseId
	 * @return
	 */
	public static Method getResponseMethodByID(int responseId) {
		return methodMapping.get(responseId);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.TESTER;
	}
}
