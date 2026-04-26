//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.utils;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.bujian.aipersnonknowledge.exception.BaseException;

import java.nio.charset.Charset;
import java.util.Map;

public class SmsHttpUtils {
    private SmsHttpUtils() {
    }

    public static SmsHttpUtils instance() {
        return SmsHttpUtils.SmsHttpHolder.INSTANCE;
    }

    public JSONObject postJson(String url, Map<String, String> headers, String body) {
        try {
            HttpResponse response = ((HttpRequest)HttpRequest.post(url).addHeaders(headers)).body(body).execute();
            Throwable var5 = null;

            JSONObject var6;
            try {
                var6 = JSONObject.parseObject(response.body());
            } catch (Throwable var16) {
                var5 = var16;
                throw var16;
            } finally {
                if (response != null) {
                    if (var5 != null) {
                        try {
                            response.close();
                        } catch (Throwable var15) {
                            var5.addSuppressed(var15);
                        }
                    } else {
                        response.close();
                    }
                }

            }

            return var6;
        } catch (Exception var18) {
            Exception e = var18;
            throw new BaseException(e.getMessage());
        }
    }

    public JSONObject postJson(String url, Map<String, String> headers, Map<String, Object> body) {
        return this.postJson(url, headers, JSONObject.toJSONString(body, new JSONWriter.Feature[0]));
    }

    public JSONObject postFrom(String url, Map<String, String> headers, Map<String, Object> body) {
        try {
            HttpResponse response = ((HttpRequest)HttpRequest.post(url).addHeaders(headers)).form(body).execute();
            Throwable var5 = null;

            JSONObject var6;
            try {
                var6 = JSONObject.parseObject(response.body());
            } catch (Throwable var16) {
                var5 = var16;
                throw var16;
            } finally {
                if (response != null) {
                    if (var5 != null) {
                        try {
                            response.close();
                        } catch (Throwable var15) {
                            var5.addSuppressed(var15);
                        }
                    } else {
                        response.close();
                    }
                }

            }

            return var6;
        } catch (Exception var18) {
            Exception e = var18;
            throw new BaseException(e.getMessage());
        }
    }

    public JSONObject postUrl(String url, Map<String, String> headers, Map<String, Object> params) {
        String urlWithParams = url + "?" + URLUtil.buildQuery(params, (Charset)null);

        try {
            HttpResponse response = ((HttpRequest)HttpRequest.post(urlWithParams).addHeaders(headers)).execute();
            Throwable var6 = null;

            JSONObject var7;
            try {
                var7 = JSONObject.parseObject(response.body());
            } catch (Throwable var17) {
                var6 = var17;
                throw var17;
            } finally {
                if (response != null) {
                    if (var6 != null) {
                        try {
                            response.close();
                        } catch (Throwable var16) {
                            var6.addSuppressed(var16);
                        }
                    } else {
                        response.close();
                    }
                }

            }

            return var7;
        } catch (Exception var19) {
            Exception e = var19;
            throw new BaseException(e.getMessage());
        }
    }

    public void safeSleep(int retryInterval) {
        ThreadUtil.safeSleep((long)retryInterval * 1000L);
    }

    private static class SmsHttpHolder {
        private static final SmsHttpUtils INSTANCE = new SmsHttpUtils();

        private SmsHttpHolder() {
        }
    }
}
