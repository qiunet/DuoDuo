package org.qiunet.cross.common.contants;

import org.qiunet.cross.node.ServerInfo;
import org.qiunet.data.redis.IRedisUtil;
import org.qiunet.utils.args.ArgumentKey;

import java.util.function.Supplier;

/***
 * cross 扫描时候的一些参数.
 *
 * @author qiunet
 * 2020-10-09 11:41
 */
public interface ScannerParamKey {
	/**ServerInfo 心跳上传需要的redis实例 Supplier.*/
	ArgumentKey<Supplier<IRedisUtil>> SERVER_NODE_REDIS_INSTANCE_SUPPLIER = new ArgumentKey<>();

	/**启动cross 自定义的serverInfo 一般测试时候需要. 正式业务properties定义即可*/
	ArgumentKey<ServerInfo> CUSTOM_SERVER_INFO  = new ArgumentKey<>();
}
