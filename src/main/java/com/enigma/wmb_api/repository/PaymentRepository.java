package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {
    List<Payment> findAllByTransactionStatusIn(List<String> transactionStatus);
}
