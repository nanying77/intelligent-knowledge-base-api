//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.load;


import com.bujian.aipersnonknowledge.sms.Interface.SmsBlend;

public class LoadServer {
    private SmsBlend smsServer;
    private int weight;
    private int currentWeight;

    public SmsBlend getSmsServer() {
        return this.smsServer;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getCurrentWeight() {
        return this.currentWeight;
    }

    public void setSmsServer(final SmsBlend smsServer) {
        this.smsServer = smsServer;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    public void setCurrentWeight(final int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof LoadServer)) {
            return false;
        } else {
            LoadServer other = (LoadServer)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getWeight() != other.getWeight()) {
                return false;
            } else if (this.getCurrentWeight() != other.getCurrentWeight()) {
                return false;
            } else {
                Object this$smsServer = this.getSmsServer();
                Object other$smsServer = other.getSmsServer();
                if (this$smsServer == null) {
                    if (other$smsServer == null) {
                        return true;
                    }
                } else if (this$smsServer.equals(other$smsServer)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LoadServer;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getWeight();
        result = result * 59 + this.getCurrentWeight();
        Object $smsServer = this.getSmsServer();
        result = result * 59 + ($smsServer == null ? 43 : $smsServer.hashCode());
        return result;
    }

    public String toString() {
        return "LoadServer(smsServer=" + this.getSmsServer() + ", weight=" + this.getWeight() + ", currentWeight=" + this.getCurrentWeight() + ")";
    }

    public LoadServer(final SmsBlend smsServer, final int weight, final int currentWeight) {
        this.smsServer = smsServer;
        this.weight = weight;
        this.currentWeight = currentWeight;
    }

    public LoadServer() {
    }
}
