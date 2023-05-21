package org.frh.ebankingbackend.repository;

import org.frh.ebankingbackend.entity.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
