package com.chensoul.security.spring.mfa;

import com.chensoul.security.spring.SecurityUtils;
import com.chensoul.security.spring.mfa.config.EmailTwoFaConfig;
import com.chensoul.security.spring.mfa.config.SmsTwoFaConfig;
import com.chensoul.security.spring.mfa.config.TwoFaConfig;
import com.chensoul.security.spring.mfa.provider.TwoFaProvider;
import com.chensoul.security.spring.mfa.provider.TwoFaProviderConfig;
import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import com.chensoul.security.spring.provider.jwt.JwtTokenFactory;
import com.chensoul.security.spring.provider.rest.MfaProperties;
import com.chensoul.security.spring.provider.rest.model.TokenPair;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.repeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultTwoFaSettingService implements TwoFaSettingService {
    private final Map<TwoFaProviderType, TwoFaProvider<TwoFaProviderConfig, TwoFaConfig>> providers = new EnumMap<>(TwoFaProviderType.class);
    private static final RuntimeException PROVIDER_NOT_CONFIGURED_ERROR = new RuntimeException("2FA provider is not configured");
    private static final RuntimeException PROVIDER_NOT_AVAILABLE_ERROR = new RuntimeException("2FA provider is not available");

    private final JwtTokenFactory tokenFactory;
    private final MfaProperties mfaProperties;

    @Autowired
    private void setProviders(Collection<TwoFaProvider> providers) {
        providers.forEach(provider -> {
            this.providers.put(provider.getType(), provider);
        });
    }

    @Override
    public void prepareVerificationCode() {
        TwoFaConfig twoFaConfig = mfaProperties.getDefaultConfig();
        TwoFaProviderConfig providerConfig = mfaProperties.getProviderConfig(twoFaConfig.getProviderType())
                .orElseThrow(() -> PROVIDER_NOT_CONFIGURED_ERROR);
        getTwoFaProvider(twoFaConfig.getProviderType()).prepareVerificationCode(SecurityUtils.getCurrentUser(), providerConfig, twoFaConfig);
    }
    @Override
    public TokenPair checkVerificationCode(String verificationCode) {
        UserDetails user = SecurityUtils.getCurrentUser();
        TwoFaConfig twoFaConfig = mfaProperties.getDefaultConfig();
        TwoFaProviderConfig providerConfig = mfaProperties.getProviderConfig(twoFaConfig.getProviderType())
                .orElseThrow(() -> PROVIDER_NOT_CONFIGURED_ERROR);

        boolean verificationSuccess = false;
        if (StringUtils.isNotBlank(verificationCode)) {
            if (StringUtils.isNumeric(verificationCode) || twoFaConfig.getProviderType()==TwoFaProviderType.BACKUP_CODE) {
                verificationSuccess = getTwoFaProvider(twoFaConfig.getProviderType()).checkVerificationCode(user, verificationCode, providerConfig, twoFaConfig);
            }
        }

        if (verificationSuccess) {
            return tokenFactory.createTokenPair(user);
        } else {
            RuntimeException error = new RuntimeException("Verification code is incorrect");
            throw error;
        }
    }
    private TwoFaProvider<TwoFaProviderConfig, TwoFaConfig> getTwoFaProvider(TwoFaProviderType providerType) {
        return Optional.ofNullable(providers.get(providerType)).orElseThrow(() -> PROVIDER_NOT_AVAILABLE_ERROR);
    }

    @Override
    public List<TwoFaAuthController.TwoFaProviderInfo> getAvailableTwoFaProviders() {
        return mfaProperties.getAllConfigs().stream().map(config -> {
                    String contact = null;
                    switch (config.getProviderType()) {
                        case SMS:
                            String phoneNumber = ((SmsTwoFaConfig) config).getPhoneNumber();
                            contact = obfuscate(phoneNumber, 2, '*', phoneNumber.indexOf('+') + 1, phoneNumber.length());
                            break;
                        case EMAIL:
                            String email = ((EmailTwoFaConfig) config).getEmail();
                            contact = obfuscate(email, 2, '*', 0, email.indexOf('@'));
                            break;
                    }
                    return TwoFaAuthController.TwoFaProviderInfo.builder()
                            .type(config.getProviderType())
                            .useByDefault(config.isUseByDefault())
                            .contact(contact)
                            .minVerificationCodeSendPeriod(mfaProperties.getMinVerificationCodeSendPeriod())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private static String obfuscate(String input, int seenMargin, char obfuscationChar,
                                    int startIndexInclusive, int endIndexExclusive) {
        String part = input.substring(startIndexInclusive, endIndexExclusive);
        String obfuscatedPart;
        if (part.length() <= seenMargin * 2) {
            obfuscatedPart = repeat(obfuscationChar, part.length());
        } else {
            obfuscatedPart = part.substring(0, seenMargin)
                    + repeat(obfuscationChar, part.length() - seenMargin * 2)
                    + part.substring(part.length() - seenMargin);
        }
        return input.substring(0, startIndexInclusive) + obfuscatedPart + input.substring(endIndexExclusive);
    }


}
