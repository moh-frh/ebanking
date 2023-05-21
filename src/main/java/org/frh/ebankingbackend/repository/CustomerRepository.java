package org.frh.ebankingbackend.repository;

import org.frh.ebankingbackend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
