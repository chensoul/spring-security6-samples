package com.chensoul.security.spring.mfa.provider;


import com.chensoul.security.spring.mfa.config.TwoFaConfig;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface TwoFaProvider<C extends TwoFaProviderConfig, A extends TwoFaConfig> {

    A generateTwoFaConfig(User user, C providerConfig);

    default void prepareVerificationCode(UserDetails user, C providerConfig, A accountConfig) {
    }

    boolean checkVerificationCode(UserDetails user, String code, C providerConfig, A accountConfig);

    default void check(String tenantId) {
    }

    TwoFaProviderType getType();

}
