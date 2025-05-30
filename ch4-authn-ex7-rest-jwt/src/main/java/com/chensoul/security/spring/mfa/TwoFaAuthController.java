package com.chensoul.security.spring.mfa;

import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import com.chensoul.security.spring.provider.rest.model.TokenPair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/2fa")
@RequiredArgsConstructor
public class TwoFaAuthController {
    private final TwoFaSettingService twoFaSettingService;

    @PostMapping("/verification/send")
    @PreAuthorize("hasAuthority('PRE_VERIFICATION_TOKEN')")
    public void requestTwoFaVerificationCode() throws Exception {
        twoFaSettingService.prepareVerificationCode();
    }

    @PostMapping("/verification/check")
    @PreAuthorize("hasAuthority('PRE_VERIFICATION_TOKEN')")
    public TokenPair checkTwoFaVerificationCode(@RequestParam String verificationCode) throws Exception {
        return twoFaSettingService.checkVerificationCode(verificationCode);
    }

    @GetMapping("/provider")
    @PreAuthorize("hasAuthority('PRE_VERIFICATION_TOKEN')")
    public List<TwoFaProviderInfo> getAvailableTwoFaProviders() {
        return twoFaSettingService.getAvailableTwoFaProviders();
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class TwoFaProviderInfo {
        private TwoFaProviderType type;
        private boolean useByDefault;
        private String contact;
        private Integer minVerificationCodeSendPeriod;
    }

}
