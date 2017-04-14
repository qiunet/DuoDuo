package org.qiunet.gametest;

import org.apache.log4j.Logger;
import org.qiunet.enums.ProtocolType;
import org.qiunet.gametest.protocol.base.NormalProtocol;
import org.qiunet.gametest.protocol.handler.IProtocolHandler;
import org.qiunet.gametest.robot.BaseRobot;
import org.qiunet.gametest.robot.IRobot;

import java.util.*;

/**
 * 测试总类
 * @author qiunet
 *         Created on 16/12/13 15:39.
 */
public abstract class GameTest {

    protected static final Logger logger = Logger.getLogger(GameTest.class);

    protected Map<ProtocolType, IProtocolHandler> handlers = new HashMap<>();

    private List<NormalProtocol> protocols  = new ArrayList<>();;

    private List<? extends BaseRobot> robots;

    private IProtocolHandler [] hanelerArrays;

    /**
     * handlers 的顺序决定执行的顺序.所以如果需要先登录http 再登录socket 的.请先给出 http 再给socket.
     * @param handlers
     */
    protected GameTest(IProtocolHandler ... handlers){
        if (handlers == null || handlers.length == 0) {
            throw new NullPointerException("IProtocolHandler length can not be zero! ");
        }

        for (IProtocolHandler handler : handlers) this.handlers.put(handler.getType(), handler);
        this.hanelerArrays = handlers;
        this.robots = getRobots();
    }

    /**
     * 子类需要初始化什么的话. 在这个方法好了
     * 不如连接池
     * 设定初始化
     * @return
     */
    protected boolean init(){
        return true;
    }
    /**有什么需要销毁 在这进行*/
    protected boolean destory(){
        if (handlers.containsKey(ProtocolType.SOCKET_TCP) || handlers.containsKey(ProtocolType.SOCKET_UDP)){
            IProtocolHandler socketHandlers [] = {handlers.get(ProtocolType.SOCKET_TCP), handlers.get(ProtocolType.SOCKET_UDP)};
            for (IProtocolHandler handler : socketHandlers) {
                if (handler == null) continue;

                for (IRobot robot : robots) {
                    handler.closeSession(robot);
                }
            }
        }
        return true;
    }
    /**
     * 负责robot授权
     * @return
     */
    private boolean login(){
        for (IRobot robot : robots) {
            for (IProtocolHandler handler : hanelerArrays) {
                boolean ret = handler.login(robot);
                if (! ret) {
                    return ret;
                }
            }
        }
        return true;
    }
    private boolean handlerProtocal(){
        for (BaseRobot robot : robots) {
            robot.setGameTest(this);
            new Thread(robot).start();
        }
        return true;
    }

    /**
     * 获得 机器人列表
     * @return
     */
    protected abstract List<? extends BaseRobot> getRobots();
    /**
     * 子类如果已经完成了初始设定等. 就可以调用该方法了
     */
    public void handler(){
        if (this.init() && this.login() && this.handlerProtocal() && this.destory()){
            logger.info("=========TEST OVER==============");
        }else {
            logger.error("=========TEST ERROR==============");
            System.exit(1);
        }

    }
    /**
     * 渠道对应的协议处理器
     * @param type
     * @return
     */
    public IProtocolHandler getHandler(ProtocolType type) {
         if (handlers.containsKey(type)) return handlers.get(type);
         throw new NullPointerException("no handler for type ["+type+"]");
    }
    /**
     * 添加协议.
     * 协议添加顺序为 执行的顺序.
     * @param protocol
     */
    public final void addProtocol(NormalProtocol protocol){
        this.protocols.add(protocol);
    }

    private List<NormalProtocol> normalProtocols;

    /**
     * 返回一个不可能更改的list. 作为机器人执行 协议 使用
     * 该List 一旦生成, 就不会被修改.
     * @return
     */
    public List<NormalProtocol> getProtocols(){
        if (normalProtocols != null) return  normalProtocols;
        normalProtocols = Collections.unmodifiableList(protocols);
        return normalProtocols;
    }
}
