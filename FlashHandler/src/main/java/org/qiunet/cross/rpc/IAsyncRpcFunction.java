package org.qiunet.cross.rpc;

import java.io.Serializable;
import java.util.function.Function;

/***
 * RPC 远程异步调用的方法
 *
 * @author qiunet
 * 2023/5/5 14:03
 */
@FunctionalInterface
public interface IAsyncRpcFunction<T extends IRpcRequest, R> extends Function<T, RpcFuture<R>>, Serializable {
}
