package com.chensoul.security.spring.mfa.provider.impl;

import com.chensoul.security.spring.mfa.config.EmailTwoFaConfig;
import com.chensoul.security.spring.mfa.provider.EmailTwoFaProviderConfig;
import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailTwoFaProvider extends OtpBasedTwoFaProvider<EmailTwoFaProviderConfig, EmailTwoFaConfig> {
//    private final MailService mailService;

    protected EmailTwoFaProvider(CacheManager cacheManager, ObjectMapper objectMapper) {
        super(cacheManager, objectMapper);
//        this.mailService = mailService;
    }

    @Override
    public EmailTwoFaConfig generateTwoFaConfig(User user, EmailTwoFaProviderConfig providerConfig) {
        EmailTwoFaConfig config = new EmailTwoFaConfig();
//        config.setEmail(user.getEmail());
        return config;
    }

    @Override
    public void check(String tenantId) {
//        try {
//            mailService.testConnection(tenantId);
//        } catch (Exception e) {
//            throw new RuntimeException("Mail service is not set up");
//        }
    }

    @Override
    protected void sendVerificationCode(UserDetails user, String verificationCode, EmailTwoFaProviderConfig providerConfig, EmailTwoFaConfig twoFaConfig) {
        log.info("send verification code {} to email {}", verificationCode, twoFaConfig.getEmail());
//        mailService.sendTwoFaVerificationEmail(twoFaConfig.getEmail(), verificationCode, providerConfig.getVerificationCodeLifetime());
    }

    @Override
    public TwoFaProviderType getType() {
        return TwoFaProviderType.EMAIL;
    }

}
