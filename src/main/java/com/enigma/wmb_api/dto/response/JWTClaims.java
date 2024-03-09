package com.enigma.wmb_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTClaims {
    private String userAccountId;
    private List<String> roles;
}
