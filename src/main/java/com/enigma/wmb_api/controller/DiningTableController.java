package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.dto.request.SearchDiningTableRequest;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.service.DiningTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/tables")
public class DiningTableController {
    private final DiningTableService tableService;

    @PostMapping
    public DiningTable createNewTable(@RequestBody DiningTable table) {
        return tableService.create(table);
    }

    @GetMapping(path = "/{id}")
    public DiningTable getTableById(@PathVariable String id) {
        return tableService.getById(id);
    }

    @GetMapping
    public List<DiningTable> getAllTable(
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchDiningTableRequest request = SearchDiningTableRequest.builder()
                .name(name)
                .build();

        return tableService.getAll(request);
    }

    @PutMapping
    public DiningTable updateTable(
            @RequestBody DiningTable table
    ) {
        return tableService.update(table);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteTable(
            @PathVariable String id
    ) {
        tableService.delete(id);
        return "OK";
    }
}
