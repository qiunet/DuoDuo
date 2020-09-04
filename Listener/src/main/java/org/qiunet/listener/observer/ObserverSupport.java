package org.qiunet.listener.observer;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/***
 *
 * 观察者对象操作类.
 * 一般存放到被观察者对象上.
 *
 * @author qiunet
 * 2020-08-31 08:15
 **/
public class ObserverSupport {

	private Map<Class<? extends IObserver>, List<ObserverWrapper>> observerMaps = Maps.newHashMap();

}
