package org.frh.ebankingbackend.service;

import jakarta.transaction.Transactional;
import org.frh.ebankingbackend.entity.BankAccount;
import org.frh.ebankingbackend.entity.CurrentAccount;
import org.frh.ebankingbackend.entity.SavingAccount;
import org.frh.ebankingbackend.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consult(){
        BankAccount bankAccount = bankAccountRepository.findById("0333a7da-ce98-4c9d-b164-7f76b03b7dae").orElse(null);
        System.out.println("**********************************************");
        //System.out.println(bankAccount.getId());
        System.out.println(bankAccount.getBalance()+" - "+bankAccount.getStatus()+" - "+bankAccount.getCustomer().getName()+" - "+bankAccount.getClass().getSimpleName());

        if(bankAccount instanceof CurrentAccount){
            System.out.println("over draft => "+((CurrentAccount) bankAccount).getOverDraft());
        }
        else if (bankAccount instanceof SavingAccount){
            System.out.println("interest rate => "+((SavingAccount) bankAccount).getInterestRate());
        }

        bankAccount.getAccountOperations().forEach(op->{
            System.out.println(op.getAmount());
        });
        System.out.println("**********************************************");
    }
}
