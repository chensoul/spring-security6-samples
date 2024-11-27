package com.chensoul.security.spring.mfa.config;

import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import java.util.LinkedHashMap;
import lombok.Data;

@Data
public class UserTwoFaSetting {
    private LinkedHashMap<TwoFaProviderType, TwoFaConfig> configs;
}
