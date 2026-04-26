package com.bujian.aipersnonknowledge.sms;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.bujian.aipersnonknowledge.exception.BaseException;
import com.bujian.aipersnonknowledge.sms.Interface.SmsBlend;
import com.bujian.aipersnonknowledge.sms.constant.SupplierType;
import com.bujian.aipersnonknowledge.sms.entity.SmsBlendModel;
import com.bujian.aipersnonknowledge.sms.load.SmsLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SmsSender {
    private static final Logger log = LoggerFactory.getLogger(SmsSender.class);
    private static final Map<SupplierType, SmsBlend> CACHE = new ConcurrentHashMap();

    public SmsSender(List<SmsBlendModel> smsBlendList) {
        Iterator var2 = smsBlendList.iterator();

        while(var2.hasNext()) {
            SmsBlendModel smsBlend = (SmsBlendModel)var2.next();
            this.registerIfAbsent(smsBlend.getSmsBlend(), smsBlend.getConfig().getWeight());
        }

    }

    public SmsBlend getBySupplier(SupplierType supplier) {
        return (SmsBlend)CACHE.get(supplier);
    }

    public SmsBlend getSmsBlend() {
        return SmsLoad.getBeanLoad().getLoadServer();
    }

    private void registerIfAbsent(SmsBlend smsBlend, Integer weight) {
        if (smsBlend == null) {
            throw new BaseException("短信服务对象不能为空");
        } else {
            CACHE.put(smsBlend.getSupplier(), smsBlend);
            SmsLoad.starConfig(smsBlend, weight);
        }
    }
}
