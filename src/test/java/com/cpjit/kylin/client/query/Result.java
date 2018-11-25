package com.cpjit.kylin.client.query;

public class Result {

    private String orgcode;
    private long total;

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


    @Override
    public String toString() {
        return "Result{" +
                "orgcode='" + orgcode + '\'' +
                ", total=" + total +
                '}';
    }


}
