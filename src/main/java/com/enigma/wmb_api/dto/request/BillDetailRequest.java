package com.enigma.wmb_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailRequest {
    private String menuId;
    private Integer qty;
}
