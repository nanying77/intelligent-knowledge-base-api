//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.entity;


import com.bujian.aipersnonknowledge.sms.Interface.SmsBlend;
import com.bujian.aipersnonknowledge.sms.config.BaseConfig;

public class SmsBlendModel {
    private SmsBlend smsBlend;
    private BaseConfig config;

    public SmsBlend getSmsBlend() {
        return this.smsBlend;
    }

    public BaseConfig getConfig() {
        return this.config;
    }

    public SmsBlendModel(final SmsBlend smsBlend, final BaseConfig config) {
        this.smsBlend = smsBlend;
        this.config = config;
    }
}
