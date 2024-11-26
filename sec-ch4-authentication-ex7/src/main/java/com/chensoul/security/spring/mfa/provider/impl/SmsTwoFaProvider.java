package com.chensoul.security.spring.mfa.provider.impl;

import com.chensoul.security.spring.mfa.config.SmsTwoFaConfig;
import com.chensoul.security.spring.mfa.provider.SmsTwoFaProviderConfig;
import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsTwoFaProvider extends OtpBasedTwoFaProvider<SmsTwoFaProviderConfig, SmsTwoFaConfig> {
//    private final SmsService smsService;

    public SmsTwoFaProvider(CacheManager cacheManager, ObjectMapper objectMapper) {
        super(cacheManager, objectMapper);
//        this.smsService = smsService;
    }

    @Override
    public SmsTwoFaConfig generateTwoFaConfig(User user, SmsTwoFaProviderConfig providerConfig) {
        return new SmsTwoFaConfig();
    }

    @Override
    protected void sendVerificationCode(UserDetails user, String verificationCode, SmsTwoFaProviderConfig providerConfig, SmsTwoFaConfig twoFaConfig) {
        log.info("send verification code {} to phoneNumber ", verificationCode, twoFaConfig.getPhoneNumber());

//        Map<String, String> messageData = Map.of(
//                "code", verificationCode,
//                "userEmail", user.getEmail()
//        );
//        String message = FormatUtils.formatVariables(providerConfig.getSmsVerificationMessageTemplate(), "${", "}", messageData);
//        String phoneNumber = twoFaConfig.getPhoneNumber();
//        try {
//            smsService.sendSms(user.getTenantId(), user.getMerchantId(), new String[]{phoneNumber}, message);
//        } catch (RuntimeException e) {
//            throw e;
//        }
    }

    @Override
    public void check(String tenantId) {
//        if (!smsService.isConfigured(tenantId)) {
//            throw new RuntimeException("SMS service is not configured");
//        }
    }

    @Override
    public TwoFaProviderType getType() {
        return TwoFaProviderType.SMS;
    }

}
