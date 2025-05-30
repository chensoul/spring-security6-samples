package com.chensoul.security.spring.mfa.config;

import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class UserTwoFaSetting {
    private LinkedHashMap<TwoFaProviderType, TwoFaConfig> configs;
}
