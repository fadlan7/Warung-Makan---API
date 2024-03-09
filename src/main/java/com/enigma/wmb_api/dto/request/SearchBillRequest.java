package com.enigma.wmb_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBillRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String id;
    private String minTransDate;
    private String maxTransDate;
    private String tableId;
    private String transType;
}
