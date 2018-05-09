package org.qiunet.utils.asyncQuene;

/**
 * @author qiunet
 *         Created on 17/3/14 11:46.
 */
public class IndexElement implements IndexQueueElement {

	private int index;

	public IndexElement (int index) {
		this.index = index;
	}

	@Override
	public boolean handler() {
		String name = Thread.currentThread().getName();
		int nameIndex = Integer.parseInt(name);
		if (nameIndex != index % IndexAsyncQueueHandler.THREAD_COUNT) {
			System.out.println("=========nameIndex ["+nameIndex+"] index ["+index+"]");
			throw new IllegalStateException("nameIndex ["+nameIndex+"] index ["+index+"]");
		}
		return true;
	}

	@Override
	public String toStr() {
		return Thread.currentThread().getName();
	}

	@Override
	public int getQueueIndex() {
		return index;
	}
}
