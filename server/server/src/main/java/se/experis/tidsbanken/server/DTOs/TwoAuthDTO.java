package se.experis.tidsbanken.server.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a DTO that consists of a Qr-code png and url for qr code
 */
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
