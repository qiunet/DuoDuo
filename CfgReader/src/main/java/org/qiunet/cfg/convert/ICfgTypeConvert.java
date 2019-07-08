package org.qiunet.cfg.convert;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;

/***
 * 按照cfg的Field顺序从上往下赋值 必须有默认空构造函数
 * 对文件里面的类型  读取  转换
 * 比如: RewardData 可能就直接读取一个string  返回一个Reward对象了.
 *
 * 仅对 private  非final 非static的字段进行赋值. 不能有set方法(抛异常)
 * @param <Obj> 返回的对象类型
 * @param <Input> 数据源
 */
public interface ICfgTypeConvert<Obj, Input> {
//	 * 处理的class 是直接读取泛型第一个参数.

	/***
	 * 可以读取的Input 返回一个对象
	 * @param fieldName json  xml可能需要用
	 * @param input
	 * @return
	 */
	Obj returnObject(String fieldName, Input input) throws Exception;
	/***
	 * 得到Obj的Class
	 * @return
	 */
	default Class<Obj> getObjClazz(){
		Type[] types = getClass().getGenericInterfaces();
		Class<Obj> handlerClass = null;
		for (Type type : types) {
			if (type instanceof ParameterizedTypeImpl
				&& ((ParameterizedTypeImpl) type).getRawType() == ICfgTypeConvert.class){
				handlerClass = (Class<Obj>) ((ParameterizedTypeImpl) type).getActualTypeArguments()[0];
			}
		}

		if (handlerClass == null) {
			throw new RuntimeException("["+getClass().getName()+"] 必须实现ICfgTypeConvert接口, 并且有对应的泛型数据.");
		}
		return handlerClass;
	}
}
