package org.frh.ebankingbackend.service;

import org.frh.ebankingbackend.dto.*;
import org.frh.ebankingbackend.entity.BankAccount;
import org.frh.ebankingbackend.entity.CurrentAccount;
import org.frh.ebankingbackend.entity.Customer;
import org.frh.ebankingbackend.entity.SavingAccount;
import org.frh.ebankingbackend.exception.BalanceNotSufficentException;
import org.frh.ebankingbackend.exception.BankAccountException;
import org.frh.ebankingbackend.exception.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long id);

    BankAccountDTO getBankAccount(String AccountId) throws BankAccountException;
    void debit(String accountId, double amount, String description) throws BankAccountException, BalanceNotSufficentException;
    void credit(String accountId, double amount, String description) throws BankAccountException, BalanceNotSufficentException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficentException, BankAccountException;
    List<BankAccountDTO> bankAccountList();

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountException;
}
