package org.frh.ebankingbackend.repository;

import org.frh.ebankingbackend.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
