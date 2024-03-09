package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.dto.request.SearchDiningTableRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.service.DiningTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.DINING_TABLE_API)
public class DiningTableController {
    private final DiningTableService tableService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping
    public ResponseEntity<CommonResponse<DiningTable>> createNewTable(@RequestBody DiningTable table) {

        DiningTable newTable = tableService.create(table);

        CommonResponse<DiningTable> response = CommonResponse.<DiningTable>builder()
                .statusCode(HttpStatus.CREATED.value())
                 .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(newTable)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<DiningTable>> getTableById(@PathVariable String id) {

        DiningTable table = tableService.getById(id);

        CommonResponse<DiningTable> response = CommonResponse.<DiningTable>builder()
                .statusCode(HttpStatus.OK.value())
                 .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(table)
                .build();

        return ResponseEntity
                .ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<DiningTable>>> getAllTable(
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchDiningTableRequest request = SearchDiningTableRequest.builder()
                .name(name)
                .build();

        List<DiningTable> tables = tableService.getAll(request);

        CommonResponse<List<DiningTable>> response = CommonResponse.<List<DiningTable>>builder()
                .statusCode(HttpStatus.OK.value())
                 .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(tables)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PutMapping
    public ResponseEntity<CommonResponse<DiningTable>> updateTable(
            @RequestBody DiningTable table
    ) {

        DiningTable updatedTable = tableService.update(table);

        CommonResponse<DiningTable> response = CommonResponse.<DiningTable>builder()
                .statusCode(HttpStatus.OK.value())
                 .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(updatedTable)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> deleteTable(
            @PathVariable String id
    ) {
        tableService.delete(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                 .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }
}
