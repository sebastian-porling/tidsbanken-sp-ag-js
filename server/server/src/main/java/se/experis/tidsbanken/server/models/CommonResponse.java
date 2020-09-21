package se.experis.tidsbanken.server.models;

public class CommonResponse {
    private String message;
    private Object data;

    public CommonResponse(){ }

    public CommonResponse(String message) {
        this.message = message;
    }

    public CommonResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public CommonResponse message(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public CommonResponse data(Object data) {
        this.data = data;
        return this;
    }

}
