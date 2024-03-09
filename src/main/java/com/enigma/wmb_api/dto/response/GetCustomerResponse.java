package com.enigma.wmb_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCustomerResponse {
    private String id;
    private String name;
    private String mobilePhoneNo;
    private Boolean isMember;
    private UserAccountResponse userAccount;
}
