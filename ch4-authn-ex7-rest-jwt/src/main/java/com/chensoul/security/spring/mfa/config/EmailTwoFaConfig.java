package com.chensoul.security.spring.mfa.config;

import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmailTwoFaConfig extends OtpBasedTwoFaConfig {

    @NotBlank
    @Email
    private String email;

    @Override
    public TwoFaProviderType getProviderType() {
        return TwoFaProviderType.EMAIL;
    }
}
