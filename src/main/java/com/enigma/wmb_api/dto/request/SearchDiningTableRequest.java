package com.enigma.wmb_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDiningTableRequest {

    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String query;
}