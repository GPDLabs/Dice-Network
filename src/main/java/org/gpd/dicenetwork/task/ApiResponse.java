package org.gpd.dicenetwork.task;

public class ApiResponse<T> {
    private String reason;
    private int code;
    private T result;

    public ApiResponse() {
    }

    public ApiResponse(String reason, int code, T result) {
        this.reason = reason;
        this.code = code;
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "reason='" + reason + '\'' +
                ", code=" + code +
                ", result=" + result +
                '}';
    }
}
