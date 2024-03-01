package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchDiningTableRequest;
import com.enigma.wmb_api.entity.DiningTable;

import java.util.List;

public interface DiningTableService {
    DiningTable create(DiningTable diningTable);

    DiningTable getById(String id);

    List<DiningTable> getAll(SearchDiningTableRequest request);

    DiningTable update(DiningTable diningTable);

    void delete(String id);
}
