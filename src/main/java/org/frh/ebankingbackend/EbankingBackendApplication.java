package org.frh.ebankingbackend;

import org.frh.ebankingbackend.dto.CurrentBankAccountDTO;
import org.frh.ebankingbackend.dto.CustomerDTO;
import org.frh.ebankingbackend.dto.SavingBankAccountDTO;
import org.frh.ebankingbackend.entity.*;
import org.frh.ebankingbackend.enums.AccountStatus;
import org.frh.ebankingbackend.enums.OperationType;
import org.frh.ebankingbackend.exception.BalanceNotSufficentException;
import org.frh.ebankingbackend.exception.BankAccountException;
import org.frh.ebankingbackend.exception.CustomerNotFoundException;
import org.frh.ebankingbackend.repository.AccountOperationRepository;
import org.frh.ebankingbackend.repository.BankAccountRepository;
import org.frh.ebankingbackend.repository.CustomerRepository;
import org.frh.ebankingbackend.service.BankAccountService;
import org.frh.ebankingbackend.service.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("mohamed", "fouaz", "adem").forEach(cus ->{
                CustomerDTO customer = new CustomerDTO();
                customer.setName(cus);
                customer.setEmail(cus+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });

            List<CustomerDTO> listCustomers = bankAccountService.listCustomers();
            listCustomers.forEach(cus -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000, 9000, cus.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*80000, 5.5, cus.getId());
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            bankAccountService.bankAccountList().forEach(account -> {
                for(int i=0; i<10; i++){
                    String accountId;
                    if(account instanceof SavingBankAccountDTO){
                        accountId = ((SavingBankAccountDTO) account).getId();
                    }else{
                        accountId = ((CurrentBankAccountDTO) account).getId();
                    }
                    try {
                        bankAccountService.credit(accountId, 100+Math.random()*900, "credit");
                        bankAccountService.debit(accountId, 10+Math.random()*90, "credit");
                    } catch (BankAccountException e) {
                        e.printStackTrace();
                    } catch (BalanceNotSufficentException e) {
                        e.printStackTrace();
                    }
                }
            });
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
