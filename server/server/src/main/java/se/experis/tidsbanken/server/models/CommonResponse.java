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

    public void message(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void data(Object data) {
        this.data = data;
    }
}
