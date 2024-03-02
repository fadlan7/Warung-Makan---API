package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.TransactionType;
import com.enigma.wmb_api.repository.TransactionTypeRepository;
import com.enigma.wmb_api.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final TransactionTypeRepository trxTypeRepository;

    @Override
    public TransactionType getById(String id) {
        return findByIdOrThrowNotFound(id.toUpperCase());
    }

    public TransactionType findByIdOrThrowNotFound(String id) {
        return trxTypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction type not found"));
    }
}
