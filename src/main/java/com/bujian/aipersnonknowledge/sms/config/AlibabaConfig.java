//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.config;

import com.bujian.aipersnonknowledge.sms.constant.SupplierType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(
    prefix = "qhkj-config.sms.aliyun"
)
public class AlibabaConfig extends BaseConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private String signature;
    private String templateCode;
    private String templateParamKey;
    private String requestUrl = "dysmsapi.aliyuncs.com";
    private String action = "SendSms";
    private String version = "2017-05-25";
    private String regionId = "cn-beijing";

    public SupplierType getSupplier() {
        return SupplierType.ALIYUN;
    }

    public AlibabaConfig() {
    }

    public String getAccessKeyId() {
        return this.accessKeyId;
    }

    public String getAccessKeySecret() {
        return this.accessKeySecret;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getTemplateCode() {
        return this.templateCode;
    }

    public String getTemplateParamKey() {
        return this.templateParamKey;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public String getAction() {
        return this.action;
    }

    public String getVersion() {
        return this.version;
    }

    public String getRegionId() {
        return this.regionId;
    }

    public void setAccessKeyId(final String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(final String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public void setSignature(final String signature) {
        this.signature = signature;
    }

    public void setTemplateCode(final String templateCode) {
        this.templateCode = templateCode;
    }

    public void setTemplateParamKey(final String templateParamKey) {
        this.templateParamKey = templateParamKey;
    }

    public void setRequestUrl(final String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public void setRegionId(final String regionId) {
        this.regionId = regionId;
    }

    public String toString() {
        return "AlibabaConfig(accessKeyId=" + this.getAccessKeyId() + ", accessKeySecret=" + this.getAccessKeySecret() + ", signature=" + this.getSignature() + ", templateCode=" + this.getTemplateCode() + ", templateParamKey=" + this.getTemplateParamKey() + ", requestUrl=" + this.getRequestUrl() + ", action=" + this.getAction() + ", version=" + this.getVersion() + ", regionId=" + this.getRegionId() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AlibabaConfig)) {
            return false;
        } else {
            AlibabaConfig other = (AlibabaConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                label121: {
                    Object this$accessKeyId = this.getAccessKeyId();
                    Object other$accessKeyId = other.getAccessKeyId();
                    if (this$accessKeyId == null) {
                        if (other$accessKeyId == null) {
                            break label121;
                        }
                    } else if (this$accessKeyId.equals(other$accessKeyId)) {
                        break label121;
                    }

                    return false;
                }

                Object this$accessKeySecret = this.getAccessKeySecret();
                Object other$accessKeySecret = other.getAccessKeySecret();
                if (this$accessKeySecret == null) {
                    if (other$accessKeySecret != null) {
                        return false;
                    }
                } else if (!this$accessKeySecret.equals(other$accessKeySecret)) {
                    return false;
                }

                label107: {
                    Object this$signature = this.getSignature();
                    Object other$signature = other.getSignature();
                    if (this$signature == null) {
                        if (other$signature == null) {
                            break label107;
                        }
                    } else if (this$signature.equals(other$signature)) {
                        break label107;
                    }

                    return false;
                }

                Object this$templateCode = this.getTemplateCode();
                Object other$templateCode = other.getTemplateCode();
                if (this$templateCode == null) {
                    if (other$templateCode != null) {
                        return false;
                    }
                } else if (!this$templateCode.equals(other$templateCode)) {
                    return false;
                }

                Object this$templateParamKey = this.getTemplateParamKey();
                Object other$templateParamKey = other.getTemplateParamKey();
                if (this$templateParamKey == null) {
                    if (other$templateParamKey != null) {
                        return false;
                    }
                } else if (!this$templateParamKey.equals(other$templateParamKey)) {
                    return false;
                }

                label86: {
                    Object this$requestUrl = this.getRequestUrl();
                    Object other$requestUrl = other.getRequestUrl();
                    if (this$requestUrl == null) {
                        if (other$requestUrl == null) {
                            break label86;
                        }
                    } else if (this$requestUrl.equals(other$requestUrl)) {
                        break label86;
                    }

                    return false;
                }

                label79: {
                    Object this$action = this.getAction();
                    Object other$action = other.getAction();
                    if (this$action == null) {
                        if (other$action == null) {
                            break label79;
                        }
                    } else if (this$action.equals(other$action)) {
                        break label79;
                    }

                    return false;
                }

                Object this$version = this.getVersion();
                Object other$version = other.getVersion();
                if (this$version == null) {
                    if (other$version != null) {
                        return false;
                    }
                } else if (!this$version.equals(other$version)) {
                    return false;
                }

                Object this$regionId = this.getRegionId();
                Object other$regionId = other.getRegionId();
                if (this$regionId == null) {
                    if (other$regionId != null) {
                        return false;
                    }
                } else if (!this$regionId.equals(other$regionId)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AlibabaConfig;
    }

    public int hashCode() {
        boolean  PRIME = true;
        int result = super.hashCode();
        Object $accessKeyId = this.getAccessKeyId();
        result = result * 59 + ($accessKeyId == null ? 43 : $accessKeyId.hashCode());
        Object $accessKeySecret = this.getAccessKeySecret();
        result = result * 59 + ($accessKeySecret == null ? 43 : $accessKeySecret.hashCode());
        Object $signature = this.getSignature();
        result = result * 59 + ($signature == null ? 43 : $signature.hashCode());
        Object $templateCode = this.getTemplateCode();
        result = result * 59 + ($templateCode == null ? 43 : $templateCode.hashCode());
        Object $templateParamKey = this.getTemplateParamKey();
        result = result * 59 + ($templateParamKey == null ? 43 : $templateParamKey.hashCode());
        Object $requestUrl = this.getRequestUrl();
        result = result * 59 + ($requestUrl == null ? 43 : $requestUrl.hashCode());
        Object $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : $action.hashCode());
        Object $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : $version.hashCode());
        Object $regionId = this.getRegionId();
        result = result * 59 + ($regionId == null ? 43 : $regionId.hashCode());
        return result;
    }
}
