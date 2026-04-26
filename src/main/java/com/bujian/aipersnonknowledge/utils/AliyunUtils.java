//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.utils;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.bujian.aipersnonknowledge.sms.config.AlibabaConfig;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class AliyunUtils {
    private static final String ALGORITHM = "HMAC-SHA1";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public AliyunUtils() {
    }

    public static String generateSendSmsRequestUrl(AlibabaConfig alibabaConfig, String templateParam, String phone, String templateCode) throws Exception {
        SDF.setTimeZone(new SimpleTimeZone(0, "GMT"));
        Map<String, String> paras = new HashMap();
        paras.put("SignatureMethod", "HMAC-SHA1");
        paras.put("SignatureNonce", UUID.randomUUID().toString());
        paras.put("AccessKeyId", alibabaConfig.getAccessKeyId());
        paras.put("SignatureVersion", "1.0");
        paras.put("Timestamp", SDF.format(new Date()));
        paras.put("Format", "JSON");
        paras.put("Action", alibabaConfig.getAction());
        paras.put("Version", alibabaConfig.getVersion());
        paras.put("RegionId", alibabaConfig.getRegionId());
        Map<String, String> paramMap = generateParamMap(alibabaConfig, phone, templateParam, templateCode);
        Map<String, String> sortParas = new TreeMap(paras);
        sortParas.putAll(paramMap);
        Iterator<String> it = sortParas.keySet().iterator();
        StringBuilder sortQueryStringTmp = new StringBuilder();

        String stringToSign;
        while(it.hasNext()) {
            stringToSign = (String)it.next();
            sortQueryStringTmp.append("&").append(specialUrlEncode(stringToSign)).append("=").append(specialUrlEncode((String)sortParas.get(stringToSign)));
        }

        stringToSign = "POST&" + specialUrlEncode("/") + "&" + specialUrlEncode(sortQueryStringTmp.substring(1));
        String signature = sign(alibabaConfig.getAccessKeySecret() + "&", stringToSign);
        StringBuilder sortQueryString = new StringBuilder();
        it = paras.keySet().iterator();

        while(it.hasNext()) {
            String key = (String)it.next();
            sortQueryString.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode((String)paras.get(key)));
        }

        return "https://" + alibabaConfig.getRequestUrl() + "/?Signature=" + specialUrlEncode(signature) + sortQueryString;
    }

    private static String specialUrlEncode(String value) throws Exception {
        return value == null ? "" : URLEncoder.encode(value, StandardCharsets.UTF_8.name()).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    private static String sign(String accessSecret, String stringToSign) {
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA1, accessSecret.getBytes());
        return hMac.digestBase64(stringToSign, StandardCharsets.UTF_8, false);
    }

    public static Map<String, String> generateParamMap(AlibabaConfig alibabaConfig, String phone, String templateParam, String templateCode) {
        Map<String, String> paramMap = new HashMap();
        paramMap.put("PhoneNumbers", phone);
        paramMap.put("SignName", alibabaConfig.getSignature());
        paramMap.put("TemplateParam", templateParam);
        paramMap.put("TemplateCode", templateCode);
        return paramMap;
    }

    public static String generateParamBody(AlibabaConfig alibabaConfig, String phone, String templateParam, String templateCode) throws Exception {
        Map<String, String> paramMap = generateParamMap(alibabaConfig, phone, templateParam, templateCode);
        StringBuilder sortQueryString = new StringBuilder();
        Iterator var6 = paramMap.keySet().iterator();

        while(var6.hasNext()) {
            String key = (String)var6.next();
            sortQueryString.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode((String)paramMap.get(key)));
        }

        return sortQueryString.substring(1);
    }
}
