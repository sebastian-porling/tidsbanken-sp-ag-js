package se.experis.tidsbanken.server.models;

public class CommonResponse {
    public Integer status;
    public String message;
    public Object data;

    public CommonResponse(){ }

    public CommonResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public CommonResponse(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
