package org.qiunet.handler.mina.server.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.qiunet.handler.mina.server.context.MinaContext;
import org.qiunet.handler.handler.acceptor.Acceptor;
import org.qiunet.handler.intercepter.HandlerIntercepter;
import org.qiunet.handler.iodata.net.AbstractRequestData;
import org.qiunet.handler.mina.server.evnet.ISessionEvent;
import org.qiunet.handler.mina.server.session.DefaultSessionBuilder;
import org.qiunet.handler.mina.server.session.ISessionBuilder;
import org.qiunet.handler.mina.server.session.MinaSession;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qiunet
 *         Created on 17/3/7 18:28.
 */
public class MinaHandler extends IoHandlerAdapter implements Runnable{
	private static final Logger logger = Logger.getLogger(MinaHandler.class);
	/**检查线程 5秒一次*/
	private static final int SLEEP_TIME=5000;
	/**最大可空闲时间 10分钟*/
	private static final int SESSION_IDLE_MAX_TIME = 60 * 1000;
	/**session 的管理 map*/
	public final Map<Long, MinaSession> sessionManager = new ConcurrentHashMap<>();
	/**可以修改的SessionBuilder */
	private ISessionBuilder sessionBuilder = new DefaultSessionBuilder();
	/**实例*/
	private volatile static MinaHandler instance;
	/**自己实现的event*/
	private ISessionEvent sessionEvent;
	/**数据接收器*/
	private Acceptor acceptor;
	
	private MinaHandler() {
		acceptor = Acceptor.create(new HandlerIntercepter());
		Thread thread = new Thread(this , "MinaHandler");
		thread.setDaemon(true);
		thread.start();
		instance = this;
	}

	public static MinaHandler getInstance() {
		if (instance == null) {
			synchronized (MinaHandler.class) {
				if (instance == null)
				{
					new MinaHandler();
				}
			}
		}
		return instance;
	}

	public void setSessionEvent(ISessionEvent sessionEvent) {
		this.sessionEvent = sessionEvent;
	}
	public void setSessionBuilder(ISessionBuilder sessionBuilder) {
		if (sessionBuilder == null) throw new NullPointerException("sessionBuilder can not be null!");
		this.sessionBuilder = sessionBuilder;
	}
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		created.incrementAndGet();
		sessionManager.put(session.getId(), sessionBuilder.build(session));
		if (needCallSessionEvent(session)) sessionEvent.sessionCreated(sessionManager.get(session.getId()));
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if (needCallSessionEvent(session)) sessionEvent.sessionClosed(sessionManager.get(session.getId()));
		sessionManager.remove(session.getId());
		closed.incrementAndGet();
		session.closeNow();
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
		if (needCallSessionEvent(session)) sessionEvent.sessionIdle(sessionManager.get(session.getId()));
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		if (needCallSessionEvent(session)) sessionEvent.sessionOpened(sessionManager.get(session.getId()));
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		sended.incrementAndGet();
		super.messageSent(session, message);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		received.incrementAndGet();
		acceptor.process(new MinaContext((AbstractRequestData) message, session));
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
		if (needCallSessionEvent(session)) sessionEvent.exceptionCaught(sessionManager.get(session.getId()), cause);
		session.closeNow();
	}

	/**
	 * 是否调用sessionEvent
	 * @param session
	 * @return
	 */
	private boolean needCallSessionEvent(IoSession session){
		return  sessionEvent != null && sessionManager.containsKey(session.getId());
	}
	/**
	 * 停止
	 */
	public void stop(){
		if (acceptor != null) acceptor.stop();
		for (MinaSession session : sessionManager.values()) {
			session.getIoSession().closeNow();
		}
		this.running = false;
	}
	/**
	 * 当前在线的用户量
	 * @return
	 */
	public int sessionSize(){
		return sessionManager.size();
	}
	private static AtomicInteger created = new AtomicInteger();
	private static AtomicInteger sended = new AtomicInteger();
	private static AtomicInteger received = new AtomicInteger();
	private static AtomicInteger closed = new AtomicInteger();
	private boolean running =true;
	@Override
	public void run() {
		while (running) {
			logger.info("SessionSize:["+MinaHandler.getInstance().sessionSize()+"]  sended:["+sended.intValue()+"] received:["+received.intValue()+"] created:["+created.intValue()+"] closed:["+closed.intValue()+"]");

			Iterator<Map.Entry<Long,MinaSession>> it = sessionManager.entrySet().iterator();
			while(it.hasNext()){
				MinaSession session = it.next().getValue();
				if(System.currentTimeMillis() - session.getLastPackageDt() > SESSION_IDLE_MAX_TIME) {
			
					it.remove();
					session.getIoSession().closeNow();
					
				}
			}
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
