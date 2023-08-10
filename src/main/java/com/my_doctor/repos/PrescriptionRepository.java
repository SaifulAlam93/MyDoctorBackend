package com.my_doctor.repos;

import com.my_doctor.domain.Prescription;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    @Query(value = " SELECT * FROM `prescription` WHERE (prescription_date BETWEEN :str AND :end ) ", nativeQuery = true)
    List<Prescription> getAllByDateBetween(@Param(value = "str") LocalDateTime str, @Param(value = "end")  LocalDateTime end);

    @Query(value = " SELECT * FROM `prescription` WHERE MONTH(prescription_date) = MONTH(CURRENT_DATE())\n" +
            " AND YEAR(prescription_date) = YEAR(CURRENT_DATE()) ", nativeQuery = true)
    List<Prescription> getAllByCurrentMonth();
}
