package org.qiunet.flash.handler.common.utils;

/***
 * 自增id计数器.
 * udp channel如果已经接收过该包.则返回false 不再重新建立udpPackages
 *
 * @Author qiunet
 * @Date Create in 2018/10/19 17:17
 **/
public class IdReceivedCounter {
	/**参与计算的位数*/
	public static final int BIT_COUNT = 60;
	/**
	 *
	 * 统计一个包是否已经建立过udpPackages. 他的id / BIT_COUNT 就是pageCount
	 * pageCount 会自增.
	 * 例如:
	 * id >= pageCount*BIT_COUNT && id < (pageCount+1) * BIT_COUNT.
	 * 判断是否已经received 是   2 ^ (id % BIT_COUNT - 1) & pageCount0 == 1
	 *
	 * id >= (pageCountxuanze+1) * BIT_COUNT && id < (pageCount + 2) * BIT_COUNT 时候
	 * 会与在 pageCounter1 上
	 */
	private int pageCount = 1;

	private long pageCounter0;
	private long pageCounter1;

	/**
	 * 是否处理过
	 * @param id
	 * @return
	 */
	public boolean canReceive(int id) {
		if(id <= 0) throw new RuntimeException("Id need more than 0");
		// 已经处理过很久的包了. 不再处理   太大的id, 不能瞎处理.可能是流包.
		if (id <= (this.pageCount - 1) * BIT_COUNT || id > (this.pageCount+2) * BIT_COUNT) return false;
		//
		if (id > ((this.pageCount+1) * BIT_COUNT) && id <= (this.pageCount+2) * BIT_COUNT) {
			this.pageCount++;
			this.pageCounter0 = this.pageCounter1;
			this.pageCounter1 = 0;
		}

		boolean zeroCounter = (id-1) / BIT_COUNT < this.pageCount;
		int p1 = (id-1) % BIT_COUNT;
		if (p1 < 0) p1 = BIT_COUNT;
		long p2 = ((long)1 << p1);
		long p3 = (zeroCounter ? pageCounter0 : pageCounter1);
		boolean ret = (p2 & p3) == 0;
		if (ret) {
			if (zeroCounter) {
				this.pageCounter0 = this.pageCounter0 + p2;
			}else {
				this.pageCounter1 = this.pageCounter1 + p2;
			}
		}
		return ret;
	}

	public int getPageCount() {
		return pageCount;
	}

	public long getPageCounter0() {
		return pageCounter0;
	}

	public long getPageCounter1() {
		return pageCounter1;
	}
}
