package com.chensoul.security.spring.mfa;

import com.chensoul.security.spring.provider.rest.model.TokenPair;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface TwoFaSettingService {
    String TWO_FA_VERIFICATION_CODE_CACHE = "twoFaVerificationCodeCache";

    void prepareVerificationCode() throws JsonProcessingException;

    TokenPair checkVerificationCode(String verificationCode);

    List<TwoFaAuthController.TwoFaProviderInfo> getAvailableTwoFaProviders();

}
