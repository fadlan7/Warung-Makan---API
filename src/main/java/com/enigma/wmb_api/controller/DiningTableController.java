package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.dto.request.SearchDiningTableRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.service.DiningTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/tables")
public class DiningTableController {
    private final DiningTableService tableService;

    @PostMapping
    public ResponseEntity<CommonResponse<DiningTable>> createNewTable(@RequestBody DiningTable table) {

        DiningTable newTable = tableService.create(table);

        CommonResponse<DiningTable> response = CommonResponse.<DiningTable>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully create new dining table")
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
                .message("Dining table found")
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
                .message("Success get all table")
                .data(tables)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<DiningTable>> updateTable(
            @RequestBody DiningTable table
    ) {

        DiningTable updatedTable = tableService.update(table);

        CommonResponse<DiningTable> response = CommonResponse.<DiningTable>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Dining table successfully modified")
                .data(updatedTable)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> deleteTable(
            @PathVariable String id
    ) {
        tableService.delete(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Dining table successfully deleted")
                .build();

        return ResponseEntity.ok(response);
    }
}
