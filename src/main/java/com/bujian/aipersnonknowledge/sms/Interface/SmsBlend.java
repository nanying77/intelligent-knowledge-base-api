//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.Interface;

import com.bujian.aipersnonknowledge.sms.callback.SmsCallBack;
import com.bujian.aipersnonknowledge.sms.constant.SupplierType;
import com.bujian.aipersnonknowledge.sms.entity.SmsResponse;

import java.util.LinkedHashMap;
import java.util.List;

public interface SmsBlend {
    SupplierType getSupplier();

    SmsResponse sendMessage(String phone, String message);

    SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages);

    SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages);

    SmsResponse massTexting(List<String> phones, String message);

    SmsResponse massTexting(List<String> phones, LinkedHashMap<String, String> messages);

    SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages);

    void sendMessageAsync(String phone, String message, SmsCallBack callBack);

    void sendMessageAsync(String phone, String message);

    void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, SmsCallBack callBack);

    void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages);

    void delayedMessage(String phone, String message, Long delayedTime);

    void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime);

    void delayMassTexting(List<String> phones, String message, Long delayedTime);

    void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime);
}
