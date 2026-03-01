package com.server.paymentservice.repository;

import com.server.paymentservice.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT CASE " +
            "WHEN COUNT(p) > 0 THEN TRUE " +
            "ELSE FALSE " +
            "END " +
            "FROM Payment p " +
            "WHERE p.orderId = :orderId " +
            "AND p.isPayed = TRUE")
    boolean existsByOrderIdAndIsPayed(Integer orderId);
}