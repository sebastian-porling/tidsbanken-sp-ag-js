package se.experis.tidsbanken.server.utils;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Component;

@Component
public class TwoFactorAuth {

    public byte[] generateQrAuth(String secret, String email) throws Exception {
        return new ZxingPngQrGenerator()
                .generate(new QrData.Builder()
                        .label(email)
                        .secret(secret)
                        .issuer("Tidsbanken")
                        .algorithm(HashingAlgorithm.SHA256)
                        .digits(6)
                        .period(30)
                        .build());
    }

    public boolean verifyCode(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }
}