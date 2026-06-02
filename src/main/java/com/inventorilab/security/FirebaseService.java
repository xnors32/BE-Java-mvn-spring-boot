package com.inventorilab.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "app.firebase.enabled", havingValue = "true")
@RequiredArgsConstructor
public class FirebaseService {

    public FirebaseToken verifyIdToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    public FirebaseToken verifyGoogleToken(String idToken) throws FirebaseAuthException {
        FirebaseToken token = verifyIdToken(idToken);
        if (token.getEmail() == null) {
            throw new FirebaseAuthException(
                com.google.firebase.ErrorCode.INVALID_ARGUMENT,
                "Token is not from Google provider",
                null,
                null,
                com.google.firebase.auth.AuthErrorCode.INVALID_ID_TOKEN
            );
        }
        return token;
    }

    public FirebaseToken verifyPhoneToken(String idToken) throws FirebaseAuthException {
        return verifyIdToken(idToken);
    }
}
