package com.enigma.wmb_api.service;


import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.dto.request.UpdateBillPaymentStatusRequest;
import org.springframework.data.domain.Page;

public interface BillService {
    BillResponse create(BillRequest request);

    Page<BillResponse> getAllBill(SearchBillRequest request);

    void updateStatus(UpdateBillPaymentStatusRequest request);
}
