package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.dto.request.SearchBillRequest;
import com.enigma.wmb_api.dto.response.BillDetailResponse;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.service.BillService;
import com.enigma.wmb_api.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/report")
public class ReportController {
    private final BillService billService;

    @GetMapping("/csv")
    public void getBill(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "minTransDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String minTransDate,
            @RequestParam(name = "maxTransDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String maxTransDate,
            @RequestParam(name = "tableId", required = false) String tableId,
            @RequestParam(name = "transType", required = false) String transType,
            HttpServletResponse response
    ) throws IOException {
        String filename = "report-data.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        SearchBillRequest request = SearchBillRequest.builder()
                .minTransDate(minTransDate)
                .maxTransDate(maxTransDate)
                .tableId(tableId)
                .transType(transType)
                .id(id)
                .build();
        List<BillResponse> bills = billService.getAllBillReport(request);

        try (CSVWriter csvWriter = new CSVWriter(response.getWriter())) {
            String[] header = {"Bill ID", "Transaction Date", "Customer ID", "Transaction Type", "Table ID", "Menu ID", "Qty", "Price", "Total"};
            csvWriter.writeNext(header);

            for (BillResponse bill : bills) {
                for (BillDetailResponse detail : bill.getBillDetails()) {
                    String[] row = {
                            bill.getId(),
                            bill.getTransDate().toString(),
                            bill.getCustomerId(),
                            bill.getTransType(),
                            bill.getTableId(),
                            detail.getMenuId(),
                            String.valueOf(detail.getQty()),
                            String.valueOf(detail.getPrice()),
                            String.valueOf(detail.getQty() * detail.getPrice())
                    };
                    csvWriter.writeNext(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTotalAmountColumn(List<String[]> data) {
        for (String[] row : data) {
            int amount = Integer.parseInt(row[8]); // Assuming amount is at index 2
            int qty = Integer.parseInt(row[7]); // Assuming qty is at index 3
            int totalAmount = amount * qty;
            row[9] = String.valueOf(totalAmount);
        }
    }
}
