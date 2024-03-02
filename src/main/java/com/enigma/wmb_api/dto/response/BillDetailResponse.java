package com.enigma.wmb_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailResponse {
    private String id;
    private String billId;
    private String menuId;
    private Integer price;

    private Integer qty;
}
