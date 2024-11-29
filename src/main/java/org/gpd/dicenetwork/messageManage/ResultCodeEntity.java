package org.gpd.dicenetwork.messageManage;

public class ResultCodeEntity {
    private String result;
    private int code;

    public ResultCodeEntity(String result, int code) {
        this.result = result;
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
