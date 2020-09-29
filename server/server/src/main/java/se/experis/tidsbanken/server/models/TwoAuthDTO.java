package se.experis.tidsbanken.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwoAuthDTO {
    private byte[] qrCodePng;
    private String uri;

    public TwoAuthDTO(byte[] qrCodePng, String uri) {
        this.qrCodePng = qrCodePng;
        this.uri = uri;
    }

    @JsonProperty("qr_code_png")
    public byte[] getQrCodePng() {
        return qrCodePng;
    }

    public String getUri() {
        return uri;
    }
}