package com.chensoul.security.spring.provider.rest;

import com.chensoul.security.spring.JacksonUtil;
import com.chensoul.security.spring.mfa.config.TwoFaConfig;
import com.chensoul.security.spring.mfa.provider.TwoFaProviderConfig;
import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@ConfigurationProperties(prefix = "security.jwt.mfa", ignoreUnknownFields = false)
public class MfaProperties {
    private boolean enabled = false;

    @Valid
    @NotEmpty
    private List<Map<String, Object>> providers;

    @NotNull
    @Min(value = 5)
    private Integer minVerificationCodeSendPeriod;

    @Min(value = 0)
    private Integer maxVerificationFailuresBeforeUserLockout;

    @NotNull
    @Min(value = 60)
    private Integer totalAllowedTimeForVerification = 3600; //sec

    @Valid
    @NotNull
    private List<Map<String, Object>> configs;

    public List<TwoFaConfig> getAllConfigs() {
        return configs.stream().map(twoFaConfig -> JacksonUtil.fromString(JacksonUtil.toString(twoFaConfig), TwoFaConfig.class)).collect(Collectors.toList());
    }

    public TwoFaConfig getDefaultConfig() {
        return getAllConfigs().stream().filter(TwoFaConfig::isUseByDefault).findAny().orElse(null);
    }

    public Optional<TwoFaProviderConfig> getProviderConfig(TwoFaProviderType providerType) {
        return Optional.ofNullable(providers)
                .flatMap(providersConfigs -> providersConfigs.stream()
                        .map(providerConfig -> JacksonUtil.fromString(JacksonUtil.toString(providerConfig), TwoFaProviderConfig.class))
                        .filter(providerConfig -> providerConfig.getProviderType().equals(providerType))
                        .findFirst());
    }

}