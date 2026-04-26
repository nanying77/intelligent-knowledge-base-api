//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
    prefix = "qhkj-config.sms"
)
@ConditionalOnProperty(
    prefix = "qhkj-config",
    name = {"sms"},
    matchIfMissing = false
)
public class SmsProperties {
    private Integer corePoolSize = 10;
    private Integer maxPoolSize = 30;
    private Integer queueCapacity = 50;

    public SmsProperties() {
    }

    public Integer getCorePoolSize() {
        return this.corePoolSize;
    }

    public Integer getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public Integer getQueueCapacity() {
        return this.queueCapacity;
    }

    public void setCorePoolSize(final Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(final Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setQueueCapacity(final Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SmsProperties)) {
            return false;
        } else {
            SmsProperties other = (SmsProperties)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$corePoolSize = this.getCorePoolSize();
                    Object other$corePoolSize = other.getCorePoolSize();
                    if (this$corePoolSize == null) {
                        if (other$corePoolSize == null) {
                            break label47;
                        }
                    } else if (this$corePoolSize.equals(other$corePoolSize)) {
                        break label47;
                    }

                    return false;
                }

                Object this$maxPoolSize = this.getMaxPoolSize();
                Object other$maxPoolSize = other.getMaxPoolSize();
                if (this$maxPoolSize == null) {
                    if (other$maxPoolSize != null) {
                        return false;
                    }
                } else if (!this$maxPoolSize.equals(other$maxPoolSize)) {
                    return false;
                }

                Object this$queueCapacity = this.getQueueCapacity();
                Object other$queueCapacity = other.getQueueCapacity();
                if (this$queueCapacity == null) {
                    if (other$queueCapacity != null) {
                        return false;
                    }
                } else if (!this$queueCapacity.equals(other$queueCapacity)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SmsProperties;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $corePoolSize = this.getCorePoolSize();
        result = result * 59 + ($corePoolSize == null ? 43 : $corePoolSize.hashCode());
        Object $maxPoolSize = this.getMaxPoolSize();
        result = result * 59 + ($maxPoolSize == null ? 43 : $maxPoolSize.hashCode());
        Object $queueCapacity = this.getQueueCapacity();
        result = result * 59 + ($queueCapacity == null ? 43 : $queueCapacity.hashCode());
        return result;
    }

    public String toString() {
        return "SmsProperties(corePoolSize=" + this.getCorePoolSize() + ", maxPoolSize=" + this.getMaxPoolSize() + ", queueCapacity=" + this.getQueueCapacity() + ")";
    }
}
