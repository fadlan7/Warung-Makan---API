package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.response.JWTClaims;
import com.enigma.wmb_api.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);
    boolean verifyJwtToken(String bearerToken);
    JWTClaims getClaimsByToken(String bearerToken);
}
