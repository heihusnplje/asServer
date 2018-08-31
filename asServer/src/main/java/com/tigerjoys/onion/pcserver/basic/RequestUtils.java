package com.tigerjoys.onion.pcserver.basic;

/**
 * 存储线程变量
 * @author chengang
 *
 */
public class RequestUtils {
	
	private static ThreadLocal<BeatContext> beatThreadLocal = new ThreadLocal<BeatContext>();
	
	/**
	 * 绑定BeatContext到当前的线程中
	 * @param beat - BeatContext
	 */
	public static void bindBeatContextToCurrentThread(BeatContext beat) {
	    beatThreadLocal.set(beat);
	}
	
	/**
	 * 获得当前的BeatContext
	 * @return BeatContext
	 */
	public static BeatContext getCurrent() {
		BeatContext beat = beatThreadLocal.get();
	    if (beat == null) throw new IllegalStateException("BeatContext");

	    return beat;
	}
	
	/**
	 * 将线程变量值移除
	 */
	public static void remove() {
		beatThreadLocal.remove();
	}

}
