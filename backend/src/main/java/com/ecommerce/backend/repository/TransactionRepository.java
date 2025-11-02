// src/main/java/com/ecommerce/backend/payment/repository/TransactionRepository.java

package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
