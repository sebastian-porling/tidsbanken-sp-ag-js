package se.experis.tidsbanken.server.utils;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Component;
import se.experis.tidsbanken.server.DTOs.TwoAuthDTO;

/**
 * Generates two factor authentication using the totp library
 */
@Component
public class TwoFactorAuth {

    /**
     * Generates a QR-code and URI for 2fa
     * @param secret unique string
     * @param email user email
     * @return TwoAuthDTO containing QR-code and URI
     * @throws Exception when QrData can't be made
     */
    public TwoAuthDTO generateQrAuth(String secret, String email) throws Exception {
        QrData data = new QrData.Builder()
                .label(email)
                .secret(secret)
                .issuer("Tidsbanken")
                .algorithm(HashingAlgorithm.SHA256)
                .digits(6)
                .period(30)
                .build();
        return new TwoAuthDTO(new ZxingPngQrGenerator().generate(data), data.getUri());
    }

    /**
     * Verifies the authentication code for 2fa
     * @param secret unique user secret
     * @param code code for authentication
     * @return true if valid
     */
    public boolean verifyCode(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }
}