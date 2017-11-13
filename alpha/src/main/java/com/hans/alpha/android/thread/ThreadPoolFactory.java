/**
 * Title: ThreadPoolFactory.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2015年7月13日 上午10:37:27
 * Version: 1.0
 */
package com.hans.alpha.android.thread;


import com.hans.alpha.android.thread.interfaces.IThreadPoolManager;
import com.hans.alpha.android.thread.manager.ThreadPoolManager;
import com.hans.alpha.utils.SingletonFactory;

/**
 * ClassName: ThreadPoolFactory
 * Description:线程池工厂类
 * Create by: 罗旭东
 * Date: 2015年7月13日 上午10:37:27
 */
public class ThreadPoolFactory {
	public static IThreadPoolManager getThreadPoolManager()
	{
		return SingletonFactory.getInstance(ThreadPoolManager.class);
	}
}
