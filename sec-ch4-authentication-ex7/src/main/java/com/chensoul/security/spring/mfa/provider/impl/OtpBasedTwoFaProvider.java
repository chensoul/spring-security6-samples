package com.chensoul.security.spring.mfa.provider.impl;

import static com.chensoul.security.spring.mfa.TwoFaSettingService.TWO_FA_VERIFICATION_CODE_CACHE;
import com.chensoul.security.spring.mfa.config.OtpBasedTwoFaConfig;
import com.chensoul.security.spring.mfa.provider.OtpBasedTwoFaProviderConfig;
import com.chensoul.security.spring.mfa.provider.TwoFaProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class OtpBasedTwoFaProvider<C extends OtpBasedTwoFaProviderConfig, A extends OtpBasedTwoFaConfig> implements TwoFaProvider<C, A> {
    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public final void prepareVerificationCode(UserDetails user, C providerConfig, A twoFaConfig) {
        String verificationCode = RandomStringUtils.randomNumeric(6);
        sendVerificationCode(user, verificationCode, providerConfig, twoFaConfig);
        cacheManager.getCache(TWO_FA_VERIFICATION_CODE_CACHE)
                .put(TWO_FA_VERIFICATION_CODE_CACHE + ":" + user.getUsername(), objectMapper.writeValueAsBytes(new Otp(System.currentTimeMillis(), verificationCode, twoFaConfig)));
    }

    protected abstract void sendVerificationCode(UserDetails user, String verificationCode, C providerConfig, A accountConfig);

    @Override
    @SneakyThrows
    public final boolean checkVerificationCode(UserDetails user, String code, C providerConfig, A twoFaConfig) {
        String correctVerificationCode = cacheManager.getCache(TWO_FA_VERIFICATION_CODE_CACHE).get(TWO_FA_VERIFICATION_CODE_CACHE + ":" + user.getUsername()).toString();
        Otp otp = objectMapper.readValue(correctVerificationCode, Otp.class);
        if (correctVerificationCode!=null) {
            if (System.currentTimeMillis() - otp.getTimestamp()
                    > TimeUnit.SECONDS.toMillis(providerConfig.getVerificationCodeExpireTime())) {
                cacheManager.getCache(TWO_FA_VERIFICATION_CODE_CACHE).evict(TWO_FA_VERIFICATION_CODE_CACHE + ":" + user.getUsername());
                return false;
            }
            if (code.equals(otp.getValue()) && twoFaConfig.equals(otp.getTwoFaConfig())) {
                cacheManager.getCache(TWO_FA_VERIFICATION_CODE_CACHE).evict(TWO_FA_VERIFICATION_CODE_CACHE + ":" + user.getUsername());
                return true;
            }
        }
        return false;
    }
    @Data
    public static class Otp implements Serializable {
        private final long timestamp;
        private final String value;
        private final OtpBasedTwoFaConfig twoFaConfig;
    }

}
