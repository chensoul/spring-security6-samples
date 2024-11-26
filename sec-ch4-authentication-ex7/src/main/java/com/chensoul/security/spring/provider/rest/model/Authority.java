package com.chensoul.security.spring.provider.rest.model;

public enum Authority {
    SYS_ADMIN,
    REFRESH_TOKEN,
    PRE_VERIFICATION_TOKEN;

    public static Authority parse(String value) {
        Authority authority = null;
        if (value!=null && value.length()!=0) {
            for (Authority current : Authority.values()) {
                if (current.name().equalsIgnoreCase(value)) {
                    authority = current;
                    break;
                }
            }
        }
        return authority;
    }
}
