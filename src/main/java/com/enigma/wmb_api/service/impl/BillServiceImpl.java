package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.response.BillDetailResponse;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.dto.response.PaymentResponse;
import com.enigma.wmb_api.dto.request.UpdateBillPaymentStatusRequest;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.repository.BillRepository;
import com.enigma.wmb_api.service.*;
import com.enigma.wmb_api.specification.BillSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    private final PaymentService paymentService;

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

        Payment payment = paymentService.createPayment(bill);
        bill.setPayment(payment);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .id(payment.getId())
                .token(payment.getToken())
                .redirectUrl(payment.getRedirectUrl())
                .transactionStatus(payment.getTransactionStatus())
                .build();

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
                .paymentResponse(paymentResponse)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BillResponse> getAllBill(SearchBillRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);

        Specification<Bill> specification = BillSpecification.getSpecification(request);

        Page<Bill> bills = billRepository.findAll(specification, pageable);

        List<BillResponse> billResponses = bills.getContent().stream().map(bill -> {
            List<BillDetailResponse> billDetailResponses = bill.getBillDetails().stream().map(billDetail -> {
                return BillDetailResponse.builder()
                        .id(billDetail.getId())
                        .billId(billDetail.getBill().getId())
                        .menuId(billDetail.getMenu().getId())
                        .qty(billDetail.getQty())
                        .price(billDetail.getPrice())
                        .build();
            }).toList();

            String tableId = null;
            if (bill.getDiningTable() != null) {
                tableId = bill.getDiningTable().getId();
            }

            return BillResponse.builder()
                    .id(bill.getId())
                    .transDate(bill.getTransDate())
                    .customerId(bill.getCustomer().getId())
                    .transType(bill.getTransactionType().getId())
                    .tableId(tableId)
                    .billDetails(billDetailResponses)
                    .build();
        }).toList();

        return new PageImpl<>(billResponses, pageable, bills.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BillResponse> getAllBillReport(SearchBillRequest request) {
        // Create a specification based on the request criteria
        Specification<Bill> specification = BillSpecification.getSpecification(request);

        // Fetch all bills that match the specification criteria
        List<Bill> bills = billRepository.findAll(specification);

        // Convert each Bill to a BillResponse and collect into a list
        List<BillResponse> billResponses = bills.stream().map(bill -> {
            List<BillDetailResponse> billDetailResponses = bill.getBillDetails().stream().map(billDetail -> {
                return BillDetailResponse.builder()
                        .id(billDetail.getId())
                        .billId(billDetail.getBill().getId())
                        .menuId(billDetail.getMenu().getId())
                        .qty(billDetail.getQty())
                        .price(billDetail.getPrice())
                        .build();
            }).toList();

            String tableId = null;
            if (bill.getDiningTable() != null) {
                tableId = bill.getDiningTable().getId();
            }

            return BillResponse.builder()
                    .id(bill.getId())
                    .transDate(bill.getTransDate())
                    .customerId(bill.getCustomer().getId())
                    .transType(bill.getTransactionType().getId())
                    .tableId(tableId)
                    .billDetails(billDetailResponses)
                    .build();
        }).toList();

        return billResponses;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(UpdateBillPaymentStatusRequest request) {
        Bill bill = billRepository.findById(request.getOrderId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        Payment payment = bill.getPayment();
        payment.setTransactionStatus(request.getTransactionStatus());
    }
}
