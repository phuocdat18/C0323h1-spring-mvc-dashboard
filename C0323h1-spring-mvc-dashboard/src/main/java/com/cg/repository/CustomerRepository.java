package com.cg.repository;

import com.cg.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllByDeletedIsFalse();
    List<Customer> findAllByIdNot(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByPhone (String phone);
}