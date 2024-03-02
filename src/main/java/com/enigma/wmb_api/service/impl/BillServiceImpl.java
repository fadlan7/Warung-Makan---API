package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.response.BillDetailResponse;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.repository.BillDetailRepository;
import com.enigma.wmb_api.repository.BillRepository;
import com.enigma.wmb_api.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final BillDetailService billDetailService;
    private final CustomerService customerService;
    private final MenuService menuService;
    private final DiningTableService tableService;
    private final TransactionTypeService trxTypeService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BillResponse create(BillRequest request) {
        Customer customer = customerService.getById(request.getCustomerId());
        DiningTable diningTable = null;
        TransactionType trxType = trxTypeService.getById("TA");

        if (request.getTableId() != null && !request.getTableId().isEmpty()) {
            diningTable = tableService.getById(request.getTableId());
            trxType = trxTypeService.getById("EI");
        }

        Bill bill = Bill.builder()
                .customer(customer)
                .transDate(new Date())
                .transactionType(trxType)
                .diningTable(diningTable)
                .build();
        billRepository.saveAndFlush(bill);

        List<BillDetail> billDetails = request.getBillDetails().stream()
                .map(detailRequest -> {
                    Menu menu = menuService.getById(detailRequest.getMenuId());

                    return BillDetail.builder()
                            .bill(bill)
                            .menu(menu)
                            .qty(detailRequest.getQty())
                            .price(menu.getPrice())
                            .build();
                }).toList();

        bill.setBillDetails(billDetails);
        billDetailService.createBulk(billDetails);

        List<BillDetailResponse> billDetailResponses = billDetails.stream().map(detail -> {
            return BillDetailResponse.builder()
                    .id(detail.getId())
                    .billId(detail.getBill().getId())
                    .menuId(detail.getMenu().getId())
                    .qty(detail.getQty())
                    .price(detail.getPrice())
                    .build();
        }).toList();

//        System.out.println("dining table" + bill.getDiningTable().getId());

        String diningTableId = null;
        if (request.getTableId() != null && !request.getTableId().isEmpty()) {
            diningTableId = bill.getDiningTable().getId();
        }

        return BillResponse.builder()
                .id(bill.getId())
                .customerId(bill.getCustomer().getId())
                .transDate(bill.getTransDate())
                .tableId(diningTableId)
                .transType(bill.getTransactionType().getId())
                .billDetails(billDetailResponses)
                .build();
    }

    @Override
    public Page<Bill> getAllBill(SearchBillRequest request) {
        return null;
    }
}
