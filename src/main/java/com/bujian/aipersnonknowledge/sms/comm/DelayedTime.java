//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.comm;

import java.util.Timer;
import java.util.TimerTask;

public class DelayedTime {
    private final Timer timer = new Timer(true);

    public DelayedTime() {
    }

    public void schedule(TimerTask task, long delay) {
        this.timer.schedule(task, delay);
    }
}
