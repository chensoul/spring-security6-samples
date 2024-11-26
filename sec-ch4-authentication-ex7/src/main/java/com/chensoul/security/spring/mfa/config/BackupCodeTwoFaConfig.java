package com.chensoul.security.spring.mfa.config;

import com.chensoul.security.spring.mfa.provider.TwoFaProviderType;
import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BackupCodeTwoFaConfig extends TwoFaConfig {

    @NotBlank
    private String codes;

    @Override
    public TwoFaProviderType getProviderType() {
        return TwoFaProviderType.BACKUP_CODE;
    }

    @JsonGetter("codes")
    public Set<String> getCodesForJson() {
        if (serializeHiddenFields) {
            return new TreeSet<>(Arrays.asList(codes.split(",")));
        } else {
            return null;
        }
    }

    @JsonGetter
    private Integer getCodesLeft() {
        if (codes!=null) {
            return getCodesForJson().size();
        } else {
            return null;
        }
    }

}
