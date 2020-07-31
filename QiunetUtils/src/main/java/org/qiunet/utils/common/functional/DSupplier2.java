package org.qiunet.utils.common.functional;

/***
 * 一个参数的supplier
 * @author qiunet
 * 2020-05-16 09:20
 **/
@FunctionalInterface
public interface DSupplier2<P1, P2, R> {

	R get(P1 p1, P2 p2);
}
