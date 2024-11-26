package com.chensoul.security.spring.mfa.config;

import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TotpTwoFaConfig extends TwoFaConfig {

    @NotBlank
    @Pattern(regexp = "otpauth://totp/(\\S+?):(\\S+?)\\?issuer=(\\S+?)&secret=(\\w+?)", message = "is invalid")
    private String authUrl;

    @Override
    public TwoFaProviderType getProviderType() {
        return TwoFaProviderType.TOTP;
    }

}

