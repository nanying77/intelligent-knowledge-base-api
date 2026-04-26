//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.factory;


import com.bujian.aipersnonknowledge.sms.Interface.SmsBlend;
import com.bujian.aipersnonknowledge.sms.universal.SupplierConfig;

public interface BaseProviderFactory<S extends SmsBlend, C extends SupplierConfig> {
    S createSms(C c);
}
