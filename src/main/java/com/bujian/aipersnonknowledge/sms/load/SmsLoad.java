//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.load;

import cn.hutool.core.bean.BeanUtil;
import com.bujian.aipersnonknowledge.sms.Interface.SmsBlend;
import com.bujian.aipersnonknowledge.sms.universal.SupplierConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SmsLoad {
    private final List<LoadServer> LoadServers = new ArrayList();
    private static final SmsLoad SMS_LOAD = new SmsLoad();

    private SmsLoad() {
    }

    public static SmsLoad newSmsLoad() {
        return new SmsLoad();
    }

    public void addLoadServer(SmsBlend loadServer, int weight) {
        this.LoadServers.add(new LoadServer(loadServer, weight, weight));
    }

    public void removeLoadServer(SmsBlend loadServer) {
        for(int i = 0; i < this.LoadServers.size(); ++i) {
            if (((LoadServer)this.LoadServers.get(i)).getSmsServer().equals(loadServer)) {
                this.LoadServers.remove(i);
                break;
            }
        }

    }

    public synchronized SmsBlend getLoadServer() {
        int totalWeight = 0;
        LoadServer selectedLoadServer = null;
        Iterator var3 = this.LoadServers.iterator();

        while(true) {
            LoadServer loadServer;
            do {
                if (!var3.hasNext()) {
                    if (selectedLoadServer == null) {
                        return null;
                    }

                    int i = selectedLoadServer.getCurrentWeight() - totalWeight;
                    selectedLoadServer.setCurrentWeight(i);
                    return selectedLoadServer.getSmsServer();
                }

                loadServer = (LoadServer)var3.next();
                totalWeight += loadServer.getWeight();
                int currentWeight = loadServer.getCurrentWeight() + loadServer.getWeight();
                loadServer.setCurrentWeight(currentWeight);
            } while(selectedLoadServer != null && loadServer.getCurrentWeight() <= selectedLoadServer.getCurrentWeight());

            selectedLoadServer = loadServer;
        }
    }

    public static void starConfig(SmsBlend smsBlend, SupplierConfig supplierConfig) {
        Map<String, Object> supplierConfigMap = BeanUtil.beanToMap(supplierConfig, new String[0]);
        Object weight = supplierConfigMap.getOrDefault("weight", 1);
        SMS_LOAD.addLoadServer(smsBlend, Integer.parseInt(weight.toString()));
    }

    public static void starConfig(SmsBlend smsBlend, Integer weight) {
        SMS_LOAD.addLoadServer(smsBlend, weight);
    }

    public static SmsLoad getBeanLoad() {
        return SMS_LOAD;
    }
}
