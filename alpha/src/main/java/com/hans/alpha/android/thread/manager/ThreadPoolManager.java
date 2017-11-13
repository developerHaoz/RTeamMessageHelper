/**
 * Title: ThreadPoolManager.java
 * Description:
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2015年7月13日 上午10:40:16
 * Version: 1.0
 */
package com.hans.alpha.android.thread.manager;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.hans.alpha.android.Log;
import com.hans.alpha.android.thread.constant.ThreadPoolConst;
import com.hans.alpha.android.thread.interfaces.IThreadPoolManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * ClassName: ThreadPoolManager
 * Description:线程池管理类
 * Create by: 罗旭东
 * Date: 2015年7月13日 上午10:40:16
 * <p/>
 * modify by changelcai on 2016/2/1
 */
public class ThreadPoolManager implements IThreadPoolManager {
    private static final String TAG = "MotherShip.ThreadPoolManager";
    /**
     * 不同类型的线程池，可以同时管理多个线程池
     */
    @SuppressLint("UseSparseArrays")
    private final Map<Integer, BaseThreadPool> threadPoolMap = new HashMap<Integer, BaseThreadPool>();
    public static Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public <T> void addTask(final Tasker<T> tasker) {
        if (tasker != null) {
            BaseThreadPool threadPool = null;
            synchronized (threadPoolMap) {
                threadPool = threadPoolMap.get(tasker.getThreadPoolType());
                //指定类型的线程池不存在则创建一个新的
                if (threadPool == null) {
                    threadPool = new BaseThreadPool(ThreadPoolParams.getParams(tasker.getThreadPoolType()));
                    threadPoolMap.put(tasker.getThreadPoolType(), threadPool);
                }
            }

            Callable<T> call = new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return postResult(tasker, tasker.doInBackground());
                }
            };

            FutureTask<T> task = new FutureTask<T>(call) {
                @Override
                protected void done() {
                    try {
                        get();
                    } catch (InterruptedException e) {
                        Log.e(TAG, e);
                        tasker.status = Tasker.Status.FINISHED;
                        tasker.abort();
                        postCancel(tasker);
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        Log.e(TAG, e.getMessage());
                        tasker.status = Tasker.Status.FINISHED;
                        e.printStackTrace();
                        throw new RuntimeException("An error occured while executing doInBackground()", e.getCause());
                    } catch (CancellationException e) {
                        tasker.abort();
                        tasker.status = Tasker.Status.FINISHED;
                        postCancel(tasker);
                        Log.e(TAG, e);
                        e.printStackTrace();
                    }
                }
            };
            tasker.setTask(task);
            threadPool.execute(task);
        }
    }

    /**
     * 将子线程结果传递到UI线程
     *
     * @param tasker
     * @param result
     * @return
     */
    private <T> T postResult(final Tasker<T> tasker, final T result) {
        tasker.status = Tasker.Status.FINISHED;
        if (tasker.getThreadPoolType() == ThreadPoolConst.THREAD_TYPE_OTHERS) {
            tasker.onPostExecute(result);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tasker.onPostExecute(result);
                }
            });
        }
        return result;
    }

    /**
     * 将子线程结果传递到UI线程
     *
     * @param tasker
     * @return
     * @mainThread
     */
    private void postCancel(final Tasker tasker) {
        if (tasker.getThreadPoolType() == ThreadPoolConst.THREAD_TYPE_OTHERS) {
            tasker.onCanceled();
        } else {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    tasker.onCanceled();
                }
            });
        }
    }

    @Override
    public BaseThreadPool getThreadPool(int threadPoolType) {
        BaseThreadPool threadPool = null;
        synchronized (threadPoolMap) {
            threadPool = threadPoolMap.get(threadPoolType);
            //指定类型的线程池不存在则创建一个新的
            if (threadPool == null) {
                threadPool = new BaseThreadPool(ThreadPoolParams.getParams(threadPoolType));
            }
        }

        return threadPool;
    }

    @Override
    public boolean removeTask(Tasker tasker) {
        BaseThreadPool threadPool = threadPoolMap.get(tasker.getThreadPoolType());

        if (threadPool != null) {
            return threadPool.remove(tasker.getTask());
        }

        return false;
    }

    @Override
    public void stopAllTask() {
        if (threadPoolMap != null) {
            for (Integer key : threadPoolMap.keySet()) {
                BaseThreadPool threadPool = threadPoolMap.get(key);

                if (threadPool != null) {
                    threadPool.shutdownNow();//试图停止所有正在执行的线程，不再处理还在池队列中等待的任务
                }
            }

            threadPoolMap.clear();
        }
    }
}
