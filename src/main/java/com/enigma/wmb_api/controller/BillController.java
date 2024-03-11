package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.request.UpdateBillPaymentStatusRequest;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.service.BillService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.Bill_API)
public class BillController {
    private final BillService billService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping
    public ResponseEntity<CommonResponse<BillResponse>> createNewBill(@RequestBody BillRequest request) {
        BillResponse newBill = billService.create(request);

        CommonResponse<BillResponse> response = CommonResponse.<BillResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(newBill)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<BillResponse>>> getAllBill(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "transDate") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "minTransDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String minTransDate,
            @RequestParam(name = "maxTransDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String maxTransDate,
            @RequestParam(name = "tableId", required = false) String tableId,
            @RequestParam(name = "transType", required = false) String transType
    ) {
        SearchBillRequest request = SearchBillRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .id(id)
                .minTransDate(minTransDate)
                .maxTransDate(maxTransDate)
                .tableId(tableId)
                .transType(transType)
                .build();
        Page<BillResponse> bills = billService.getAllBill(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(bills.getTotalPages())
                .totalElement(bills.getTotalElements())
                .page(bills.getPageable().getPageNumber() + 1)
                .size(bills.getPageable().getPageSize())
                .hasNext(bills.hasNext())
                .hasPrevious(bills.hasPrevious())
                .build();

        CommonResponse<List<BillResponse>> response = CommonResponse.<List<BillResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(bills.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/status")
    public ResponseEntity<CommonResponse<?>> updateStatus(
            @RequestBody Map<String, Object> request
    ) {
        UpdateBillPaymentStatusRequest updatePaymentStatus = UpdateBillPaymentStatusRequest.builder()
                .orderId(request.get("order_id").toString())
                .transactionStatus(request.get("transaction_status").toString())
                .build();

        billService.updateStatus(updatePaymentStatus);

        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build());
    }

}
