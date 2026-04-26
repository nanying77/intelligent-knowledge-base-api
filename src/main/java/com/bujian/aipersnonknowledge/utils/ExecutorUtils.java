//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.utils;

import com.bujian.aipersnonknowledge.sms.comm.DelayedTime;
import com.bujian.aipersnonknowledge.sms.properties.SmsProperties;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {
    private static DelayedTime delayedTime;
    private static SmsProperties smsProperties;
    private static Executor executor;

    public ExecutorUtils() {
    }

    public static DelayedTime getDelayedTime() {
        if (delayedTime == null) {
            delayedTime = new DelayedTime();
        }

        return delayedTime;
    }

    public static Executor getExecutor() {
        if (executor == null && smsProperties != null) {
            ThreadPoolExecutor ex = new ThreadPoolExecutor(smsProperties.getCorePoolSize(), smsProperties.getMaxPoolSize(), (long)smsProperties.getQueueCapacity(), TimeUnit.SECONDS, new ArrayBlockingQueue(smsProperties.getMaxPoolSize()));
            ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            executor = ex;
        }

        return executor;
    }

    public static void setSmsProperties(final SmsProperties smsProperties) {
        ExecutorUtils.smsProperties = smsProperties;
    }
}
