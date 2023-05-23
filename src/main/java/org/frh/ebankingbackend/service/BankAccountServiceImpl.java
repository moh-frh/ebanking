package org.frh.ebankingbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.frh.ebankingbackend.dto.CustomerDTO;
import org.frh.ebankingbackend.entity.*;
import org.frh.ebankingbackend.enums.AccountStatus;
import org.frh.ebankingbackend.enums.OperationType;
import org.frh.ebankingbackend.exception.BalanceNotSufficentException;
import org.frh.ebankingbackend.exception.BankAccountException;
import org.frh.ebankingbackend.exception.CustomerNotFoundException;
import org.frh.ebankingbackend.mapper.BankAccountMapperImpl;
import org.frh.ebankingbackend.repository.AccountOperationRepository;
import org.frh.ebankingbackend.repository.BankAccountRepository;
import org.frh.ebankingbackend.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    private BankAccountMapperImpl dtoMapper;

    //deleted bcz we use @Slf4j
    //Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository, BankAccountMapperImpl dtoMapper){
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.dtoMapper = dtoMapper;
    }
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer !!");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        CurrentAccount currentAccount = new CurrentAccount();
        Customer customer = customerRepository.findById(customerId).orElseThrow();

        if(customer == null){
            throw new CustomerNotFoundException("customer not found");
        }

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setOverDraft(overDraft);

        currentAccount.setCustomer(customer);

        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return savedBankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        SavingAccount savingAccount = new SavingAccount();
        Customer customer = customerRepository.findById(customerId).orElseThrow();

        if(customer == null){
            throw new CustomerNotFoundException("customer not found");
        }

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setInterestRate(interestRate);

        savingAccount.setCustomer(customer);

        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return savedBankAccount;
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        //method1 :
        // List<CustomerDTO> customerDTOS = customers.stream()
        //  .map( cust->{ dtoMapper.fromCustomer(cust); })
        //  .collect(Collectors.toList());
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer:customers){
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer !!");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long id){
        customerRepository.deleteById(id);
    }

    @Override
    public BankAccount getBankAccount(String AccountId) throws BankAccountException {
        BankAccount bankAccount =  bankAccountRepository.findById(AccountId)
                .orElseThrow(()->new BankAccountException("bank account not found"));

        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountException, BalanceNotSufficentException {
        BankAccount bankAccount = getBankAccount(accountId);
        if(bankAccount.getBalance() < amount){
            throw new BalanceNotSufficentException("solde not sufficiient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        //update the new bank account balance
        bankAccount.setBalance(bankAccount.getBalance());
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountException {
        BankAccount bankAccount = getBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        //update the new bank account balance
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficentException, BankAccountException {

        BankAccount bankAccountSource = bankAccountRepository.findById(accountIdSource).orElse(null);
        if(bankAccountSource.getBalance() < amount){
            throw new BalanceNotSufficentException("balance to transfer not sufficient in source account");
        }
        debit(accountIdSource, amount, "debit-transaction to: "+accountIdDestination);
        credit(accountIdDestination, amount, "credit-transaction from: "+accountIdSource);
    }

    @Override
    public List<BankAccount> bankAccountList() {
        return bankAccountRepository.findAll();
    }


}
