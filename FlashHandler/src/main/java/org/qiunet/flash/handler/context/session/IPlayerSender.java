package org.qiunet.flash.handler.context.session;

import org.qiunet.flash.handler.context.sender.ISessionHolder;
import org.qiunet.flash.handler.context.session.kcp.IKcpSessionHolder;

/***
 * 玩家. 一般可能有kcp 和 tcp的实现.
 * 持有玩家对象的对象,也可以实现该接口.
 * 从而有发送消息的功能.
 *
 * @author qiunet
 * 2023/4/5 16:35
 */
public interface IPlayerSender extends ISessionHolder, IKcpSessionHolder {
}
