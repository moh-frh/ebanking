package org.frh.ebankingbackend.service;

import org.frh.ebankingbackend.dto.CustomerDTO;
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
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long id);

    BankAccount getBankAccount(String AccountId) throws BankAccountException;
    void debit(String accountId, double amount, String description) throws BankAccountException, BalanceNotSufficentException;
    void credit(String accountId, double amount, String description) throws BankAccountException, BalanceNotSufficentException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficentException, BankAccountException;
    List<BankAccount> bankAccountList();

}
