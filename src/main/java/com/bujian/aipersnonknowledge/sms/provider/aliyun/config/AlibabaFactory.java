//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.provider.aliyun.config;


import com.bujian.aipersnonknowledge.sms.config.AlibabaConfig;
import com.bujian.aipersnonknowledge.sms.factory.BaseProviderFactory;
import com.bujian.aipersnonknowledge.sms.provider.aliyun.impl.AlibabaSmsImpl;

public class AlibabaFactory implements BaseProviderFactory<AlibabaSmsImpl, AlibabaConfig> {
    private static final AlibabaFactory INSTANCE = new AlibabaFactory();

    public static AlibabaFactory instance() {
        return INSTANCE;
    }

    public AlibabaSmsImpl createSms(AlibabaConfig alibabaConfig) {
        return new AlibabaSmsImpl(alibabaConfig);
    }

    private AlibabaFactory() {
    }
}
