package com.oyh.pregnancydiary.repository;

import com.oyh.pregnancydiary.entity.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
    List<BookOrder> findByPregnancyId(long pregnancyId);
    Optional<BookOrder> findBySweetbookOrderId(String sweetbookOrderId);
}
