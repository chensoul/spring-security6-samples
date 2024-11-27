package com.chensoul.security.spring.mfa.config;

import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmsTwoFaConfig extends OtpBasedTwoFaConfig {

    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "is not of E.164 format")
    private String phoneNumber;

    @Override
    public TwoFaProviderType getProviderType() {
        return TwoFaProviderType.SMS;
    }

}
