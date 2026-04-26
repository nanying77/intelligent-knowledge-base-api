//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.sms.entity;

import com.alibaba.fastjson2.JSONObject;

public class SmsResponse {
    private boolean success;
    private JSONObject data;
    private String supplier;

    public SmsResponse() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public JSONObject getData() {
        return this.data;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public void setData(final JSONObject data) {
        this.data = data;
    }

    public void setSupplier(final String supplier) {
        this.supplier = supplier;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SmsResponse)) {
            return false;
        } else {
            SmsResponse other = (SmsResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isSuccess() != other.isSuccess()) {
                return false;
            } else {
                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                Object this$supplier = this.getSupplier();
                Object other$supplier = other.getSupplier();
                if (this$supplier == null) {
                    if (other$supplier != null) {
                        return false;
                    }
                } else if (!this$supplier.equals(other$supplier)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SmsResponse;
    }

    public int hashCode() {
       boolean PRIME = true;
        int result = 1;
        result = result * 59 + (this.isSuccess() ? 79 : 97);
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        Object $supplier = this.getSupplier();
        result = result * 59 + ($supplier == null ? 43 : $supplier.hashCode());
        return result;
    }

    public String toString() {
        return "SmsResponse(success=" + this.isSuccess() + ", data=" + this.getData() + ", supplier=" + this.getSupplier() + ")";
    }
}
