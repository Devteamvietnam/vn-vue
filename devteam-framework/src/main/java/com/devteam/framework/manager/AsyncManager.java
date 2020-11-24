package com.devteam.framework.manager;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.devteam.common.utils.Threads;
import com.devteam.common.utils.spring.SpringUtils;

/**
 * Asynchronous task manager
 */
public class AsyncManager
{
    /**
     * Operation delay 10 milliseconds
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * Asynchronous operation task scheduling thread pool
     */
    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    /**
     * Singleton mode
     */
    private AsyncManager(){}

    private static AsyncManager me = new AsyncManager();

    public static AsyncManager me()
    {
        return me;
    }

    /**
     * Perform tasks
     *
     * @param task task
     */
    public void execute(TimerTask task)
    {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop task thread pool
     */
    public void shutdown()
    {
        Threads.shutdownAndAwaitTermination(executor);
    }
}
