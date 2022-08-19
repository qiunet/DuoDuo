package org.qiunet.flash.handler.netty.client.kcp;

import com.google.common.collect.Maps;
import org.qiunet.utils.string.StringUtil;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 *
 * @author qiunet
 * 2022/8/17 15:13
 */
public class KcpDetailPrinter {

	private final Map<Integer, KcpDetail> map = Maps.newConcurrentMap();
	public KcpDetailPrinter(){}

	public KcpDetail get(int convId) {
		return map.computeIfAbsent(convId, KcpDetail::new);
	}

	public void inIncr(int convId) {
		get(convId).in.incrementAndGet();
	}

	public void outIncr(int convId) {
		get(convId).in.incrementAndGet();
	}

	public void printDetail() {
		map.forEach((key, val) -> System.out.println("Print Detail: "+val.print()));
	}

	public static class KcpDetail {
		public final int convId;

		public final AtomicInteger in = new AtomicInteger();

		public final AtomicInteger out = new AtomicInteger();

		public KcpDetail(int convId) {
			this.convId = convId;
		}

		public String print() {
			return StringUtil.slf4jFormat("ConvId: {}  in:{}  out:{}", convId, in.get(), out.get());
		}
	}
}
