package org.qiunet.cross.rpc;

import java.io.Serializable;
import java.util.function.Function;

/***
 * RPC 调用的方法
 *
 * @author qiunet
 * 2023/5/5 14:03
 */
@FunctionalInterface
public interface IRpcFunction<T extends IRpcRequest, R> extends Function<T, R>, Serializable {
}
