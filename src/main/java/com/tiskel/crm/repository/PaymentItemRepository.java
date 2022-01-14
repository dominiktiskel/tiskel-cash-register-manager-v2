package com.tiskel.crm.repository;

import com.tiskel.crm.domain.PaymentItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentItemRepository extends JpaRepository<PaymentItem, Long> {}
