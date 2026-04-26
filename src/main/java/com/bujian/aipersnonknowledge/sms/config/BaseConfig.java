//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.config;


import com.bujian.aipersnonknowledge.exception.BaseException;
import com.bujian.aipersnonknowledge.sms.universal.SupplierConfig;

public abstract class BaseConfig implements SupplierConfig {
    private Integer weight = 1;
    private int retryInterval = 5;
    private int maxRetries = 0;

    public void setRetryInterval(int retryInterval) {
        if (retryInterval <= 0) {
            throw new BaseException("重试间隔必须大于0秒");
        } else {
            this.retryInterval = retryInterval;
        }
    }

    public void setMaxRetries(int maxRetries) {
        if (maxRetries < 0) {
            throw new BaseException("重试次数不能小于0次");
        } else {
            this.maxRetries = maxRetries;
        }
    }

    public BaseConfig() {
    }

    public Integer getWeight() {
        return this.weight;
    }

    public int getRetryInterval() {
        return this.retryInterval;
    }

    public int getMaxRetries() {
        return this.maxRetries;
    }

    public void setWeight(final Integer weight) {
        this.weight = weight;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseConfig)) {
            return false;
        } else {
            BaseConfig other = (BaseConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getRetryInterval() != other.getRetryInterval()) {
                return false;
            } else if (this.getMaxRetries() != other.getMaxRetries()) {
                return false;
            } else {
                Object this$weight = this.getWeight();
                Object other$weight = other.getWeight();
                if (this$weight == null) {
                    if (other$weight == null) {
                        return true;
                    }
                } else if (this$weight.equals(other$weight)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BaseConfig;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getRetryInterval();
        result = result * 59 + this.getMaxRetries();
        Object $weight = this.getWeight();
        result = result * 59 + ($weight == null ? 43 : $weight.hashCode());
        return result;
    }

    public String toString() {
        return "BaseConfig(weight=" + this.getWeight() + ", retryInterval=" + this.getRetryInterval() + ", maxRetries=" + this.getMaxRetries() + ")";
    }
}
