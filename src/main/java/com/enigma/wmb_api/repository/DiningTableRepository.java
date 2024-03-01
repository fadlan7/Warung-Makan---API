package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, String>, JpaSpecificationExecutor<DiningTable> {
}
