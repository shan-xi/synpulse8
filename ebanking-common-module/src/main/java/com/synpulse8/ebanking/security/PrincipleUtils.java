package com.synpulse8.ebanking.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrincipleUtils {
    public String getUid() {
        var uid = "";
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof EbankingPrincipal ebankingPrincipal) {
            uid = ebankingPrincipal.getName();
        }
        return uid;
    }
}
