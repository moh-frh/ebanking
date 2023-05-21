package org.frh.ebankingbackend;

import org.frh.ebankingbackend.entity.*;
import org.frh.ebankingbackend.enums.AccountStatus;
import org.frh.ebankingbackend.enums.OperationType;
import org.frh.ebankingbackend.repository.AccountOperationRepository;
import org.frh.ebankingbackend.repository.BankAccountRepository;
import org.frh.ebankingbackend.repository.CustomerRepository;
import org.frh.ebankingbackend.service.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankService bankService){
        return args -> {
            bankService.consult();
        };
    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            // create 3 customers
            Stream.of("mohamed", "raouf", "aziz").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");

                customerRepository.save(customer);
            });

            // create accounts
            customerRepository.findAll().forEach(customer->{
                // create current account
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                // create saving account
                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });

            bankAccountRepository.findAll().forEach(account->{
                // we create 5 operations for each account
                for(int i=0; i<10; i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setDate(new Date());
                    accountOperation.setAmount(Math.random()*10000);
                    accountOperation.setType(Math.random()<0.5 ? OperationType.CREDIT : OperationType.DEBIT);
                    accountOperation.setBankAccount(account);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }

}
