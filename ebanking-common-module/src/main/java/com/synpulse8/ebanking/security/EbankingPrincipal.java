package com.synpulse8.ebanking.security;

import javax.security.auth.Subject;
import java.security.Principal;

public class EbankingPrincipal implements Principal {
    private final String uid;

    public EbankingPrincipal(String uid) {
        this.uid = uid;
    }

    @Override
    public String getName() {
        return this.uid;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
