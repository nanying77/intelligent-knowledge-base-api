//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bujian.aipersnonknowledge.sms.config.AlibabaConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SmsUtils {
    public SmsUtils() {
    }

    public static String[] listToArray(List<String> list) {
        List<String> toStr = new ArrayList();
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            toStr.add(StrUtil.addPrefixIfNot(s, "+86"));
        }

        return (String[])toStr.toArray(new String[list.size()]);
    }

    public static String arrayToString(List<String> list) {
        return CollUtil.join(list, ",", (str) -> {
            return StrUtil.addPrefixIfNot(str, "+86");
        });
    }

    public static boolean loadAliyun(AlibabaConfig alibabaConfig) {
        return alibabaConfig.getAccessKeyId() != null && alibabaConfig.getAccessKeySecret() != null && alibabaConfig.getSignature() != null && alibabaConfig.getTemplateCode() != null;
    }
}
