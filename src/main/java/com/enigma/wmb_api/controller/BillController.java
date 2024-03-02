package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/bills")
public class BillController {
    private final BillService billService;

    @PostMapping
    public ResponseEntity<CommonResponse<BillResponse>> createNewBill(@RequestBody BillRequest request) {
        BillResponse newBill = billService.create(request);

        CommonResponse<BillResponse> response = CommonResponse.<BillResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully create new bill")
                .data(newBill)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
