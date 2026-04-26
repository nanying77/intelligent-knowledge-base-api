//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.blend;

import com.bujian.aipersnonknowledge.sms.Interface.SmsBlend;
import com.bujian.aipersnonknowledge.sms.callback.SmsCallBack;
import com.bujian.aipersnonknowledge.sms.comm.DelayedTime;
import com.bujian.aipersnonknowledge.sms.entity.SmsResponse;
import com.bujian.aipersnonknowledge.sms.universal.SupplierConfig;
import com.bujian.aipersnonknowledge.utils.ExecutorUtils;
import com.bujian.aipersnonknowledge.utils.SmsHttpUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AbstractSmsBlend<C extends SupplierConfig> implements SmsBlend {
    private final C config;
    protected final Executor pool;
    protected final DelayedTime delayed;
    protected final SmsHttpUtils http = SmsHttpUtils.instance();

    protected AbstractSmsBlend(C config, Executor pool, DelayedTime delayed) {
        this.config = config;
        this.pool = pool;
        this.delayed = delayed;
    }

    protected AbstractSmsBlend(C config) {
        this.config = config;
        this.pool = ExecutorUtils.getExecutor();
        this.delayed = ExecutorUtils.getDelayedTime();
    }

    public abstract SmsResponse sendMessage(String phone, String message);

    public abstract SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages);

    public abstract SmsResponse massTexting(List<String> phones, String message);

    public abstract SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages);

    public final void sendMessageAsync(String phone, String message, SmsCallBack callBack) {
        CompletableFuture<SmsResponse> smsResponseCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return this.sendMessage(phone, message);
        }, this.pool);
        smsResponseCompletableFuture.thenAcceptAsync(callBack::callBack);
    }

    public final void sendMessageAsync(String phone, String message) {
        this.pool.execute(() -> {
            this.sendMessage(phone, message);
        });
    }

    public final void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, SmsCallBack callBack) {
        CompletableFuture<SmsResponse> smsResponseCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return this.sendMessage(phone, templateId, messages);
        }, this.pool);
        smsResponseCompletableFuture.thenAcceptAsync(callBack::callBack);
    }

    public final void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages) {
        this.pool.execute(() -> {
            this.sendMessage(phone, templateId, messages);
        });
    }

    public final void delayedMessage(final String phone, final String message, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            public void run() {
                AbstractSmsBlend.this.sendMessage(phone, message);
            }
        }, delayedTime);
    }

    public final void delayedMessage(final String phone, final String templateId, final LinkedHashMap<String, String> messages, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            public void run() {
                AbstractSmsBlend.this.sendMessage(phone, templateId, messages);
            }
        }, delayedTime);
    }

    public final void delayMassTexting(final List<String> phones, final String message, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            public void run() {
                AbstractSmsBlend.this.massTexting(phones, message);
            }
        }, delayedTime);
    }

    public final void delayMassTexting(final List<String> phones, final String templateId, final LinkedHashMap<String, String> messages, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            public void run() {
                AbstractSmsBlend.this.massTexting(phones, templateId, messages);
            }
        }, delayedTime);
    }

    public C getConfig() {
        return this.config;
    }
}
