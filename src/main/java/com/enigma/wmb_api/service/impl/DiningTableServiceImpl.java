package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.SearchDiningTableRequest;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.repository.DiningTableRepository;
import com.enigma.wmb_api.service.DiningTableService;
import com.enigma.wmb_api.specification.DiningTableSpecification;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiningTableServiceImpl implements DiningTableService {

    private final DiningTableRepository tableRepository;
    private final ValidationUtil validationUtil;

    @Override
    public DiningTable create(DiningTable diningTable) {
        validationUtil.validate(diningTable);

        return tableRepository.save(diningTable);
    }

    @Override
    public DiningTable getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<DiningTable> getAll(SearchDiningTableRequest request) {
        Specification<DiningTable> specification = DiningTableSpecification.getSpecification(request);
        return tableRepository.findAll(specification);
    }

    @Override
    public DiningTable update(DiningTable diningTable) {
        findByIdOrThrowNotFound(diningTable.getId());
        validationUtil.validate(diningTable);

        return tableRepository.save(diningTable);
    }

    @Override
    public void delete(String id) {
        DiningTable table = findByIdOrThrowNotFound(id);
        tableRepository.delete(table);
    }

    public DiningTable findByIdOrThrowNotFound(String id) {
        return tableRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dining table not found"));
    }
}
