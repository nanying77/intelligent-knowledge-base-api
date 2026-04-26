//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.provider.aliyun.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.bujian.aipersnonknowledge.exception.BaseException;
import com.bujian.aipersnonknowledge.sms.blend.AbstractSmsBlend;
import com.bujian.aipersnonknowledge.sms.comm.DelayedTime;
import com.bujian.aipersnonknowledge.sms.config.AlibabaConfig;
import com.bujian.aipersnonknowledge.sms.constant.SupplierType;
import com.bujian.aipersnonknowledge.sms.entity.SmsResponse;
import com.bujian.aipersnonknowledge.utils.AliyunUtils;
import com.bujian.aipersnonknowledge.utils.SmsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class AlibabaSmsImpl extends AbstractSmsBlend<AlibabaConfig> {
    private static final Logger log = LoggerFactory.getLogger(AlibabaSmsImpl.class);
    private int retry = 0;

    public AlibabaSmsImpl(AlibabaConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public AlibabaSmsImpl(AlibabaConfig config) {
        super(config);
    }

    public SupplierType getSupplier() {
        return SupplierType.ALIYUN;
    }

    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap();
        map.put(((AlibabaConfig)this.getConfig()).getTemplateParamKey(), message);
        return this.sendMessage(phone, ((AlibabaConfig)this.getConfig()).getTemplateCode(), map);
    }

    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSONObject.toJSONString(messages, new JSONWriter.Feature[0]);
        return this.getSmsResponse(phone, messageStr, templateId);
    }

    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        return this.sendMessage(phone, ((AlibabaConfig)this.getConfig()).getTemplateCode(), messages);
    }

    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap();
        map.put(((AlibabaConfig)this.getConfig()).getTemplateParamKey(), message);
        return this.massTexting(phones, ((AlibabaConfig)this.getConfig()).getTemplateCode(), map);
    }

    public SmsResponse massTexting(List<String> phones, LinkedHashMap<String, String> messages) {
        return this.massTexting(phones, ((AlibabaConfig)this.getConfig()).getTemplateCode(), messages);
    }

    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSONObject.toJSONString(messages, new JSONWriter.Feature[0]);
        return this.getSmsResponse(SmsUtils.arrayToString(phones), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String templateParam, String templateCode) {
        String requestUrl;
        String paramStr;
        try {
            requestUrl = AliyunUtils.generateSendSmsRequestUrl((AlibabaConfig)this.getConfig(), templateParam, phone, templateCode);
            paramStr = AliyunUtils.generateParamBody((AlibabaConfig)this.getConfig(), phone, templateParam, templateCode);
        } catch (Exception var9) {
            Exception e = var9;
            log.error("aliyun send message error", e);
            throw new BaseException(e.getMessage());
        }

        log.debug("requestUrl {}", requestUrl);

        try {
            Map<String, String> headers = new LinkedHashMap(1);
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            SmsResponse smsResponse = this.getResponse(this.http.postJson(requestUrl, headers, paramStr));
            if (!smsResponse.isSuccess() && this.retry != ((AlibabaConfig)this.getConfig()).getMaxRetries()) {
                return this.requestRetry(phone, templateParam, templateCode);
            } else {
                this.retry = 0;
                return smsResponse;
            }
        } catch (BaseException var8) {
            return this.requestRetry(phone, templateParam, templateCode);
        }
    }

    private SmsResponse requestRetry(String phone, String templateParam, String templateCode) {
        this.http.safeSleep(((AlibabaConfig)this.getConfig()).getRetryInterval());
        ++this.retry;
        log.warn("短信第 {" + this.retry + "} 次重新发送");
        return this.getSmsResponse(phone, templateParam, templateCode);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess("OK".equals(resJson.getString("Code")));
        smsResponse.setData(resJson);
        smsResponse.setSupplier(this.getSupplier().getType());
        return smsResponse;
    }
}
