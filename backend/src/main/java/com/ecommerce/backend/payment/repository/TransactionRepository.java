// src/main/java/com/ecommerce/backend/payment/repository/TransactionRepository.java

package com.ecommerce.backend.payment.repository;

import com.ecommerce.backend.payment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}