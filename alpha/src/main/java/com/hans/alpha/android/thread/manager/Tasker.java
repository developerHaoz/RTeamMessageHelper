/**
 * Title: Tasker.java
 * Description:
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2015年7月13日 上午10:42:59
 * Version: 1.0
 */
package com.hans.alpha.android.thread.manager;

import com.hans.alpha.android.thread.ThreadPoolFactory;
import com.hans.alpha.android.thread.constant.ThreadPoolConst;

import java.util.concurrent.FutureTask;

/**
 * 异步任务类
 *
 * @param <T> create by changelcai on 2016/2/1
 */
public abstract class Tasker<T> {

    protected abstract T doInBackground();

    protected void onPostExecute(T data) {
    }

    protected void onCanceled() {
    }

    protected void abort() {
    }

    /**
     * 线程池类型
     */
    protected int threadPoolType;

    protected String taskName = null;

    public Tasker(int threadPoolType, String threadTaskName) {
        initThreadTaskObject(threadPoolType, threadTaskName);
    }

    public Tasker(int threadPoolType) {
        initThreadTaskObject(threadPoolType, this.toString());
    }

    /**
     * 在默认线程池中执行
     */
    public Tasker() {
        initThreadTaskObject(ThreadPoolConst.THREAD_TYPE_WORK, this.toString());
    }

    /**
     * 初始化线程任务
     *
     * @param threadPoolType 线程池类型
     * @param threadTaskName 线程任务名称
     */
    private void initThreadTaskObject(int threadPoolType, String threadTaskName) {
        status = Status.PENDING;
        this.threadPoolType = threadPoolType;
        String name = ThreadPoolParams.getParams(threadPoolType).name();
        if (threadTaskName != null) {
            name = name + "_" + threadTaskName;
        }

        setTaskName(name);
    }

    /**
     * 取得线程池类型
     *
     * @return
     */
    public int getThreadPoolType() {
        return threadPoolType;
    }

    /**
     * 开始任务
     */
    public void start() {
        status = Status.RUNNING;
        ThreadPoolFactory.getThreadPoolManager().addTask(this);
    }


    /**
     * 取消任务
     */
    public void cancel() {
        status = Status.FINISHED;
        ThreadPoolFactory.getThreadPoolManager().removeTask(this);
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    private FutureTask<T> task;

    /**
     * 设置当前任务
     *
     * @param task
     */
    public void setTask(FutureTask<T> task) {
        this.task = task;
    }

    /**
     * 获取当前任务
     *
     * @return
     */
    public FutureTask<T> getTask() {
        return task;
    }


    public Status status;

    public Status getStatus() {
        return status;
    }

    public enum Status {
        /**
         * Indicates that the task has not been executed yet.
         */
        PENDING,
        /**
         * Indicates that the task is running.
         */
        RUNNING,
        /**
         * Indicates that the task has finished.
         */
        FINISHED,
    }
}
