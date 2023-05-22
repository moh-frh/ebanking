package org.frh.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.frh.ebankingbackend.dto.CustomerDTO;
import org.frh.ebankingbackend.entity.Customer;
import org.frh.ebankingbackend.service.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> listCustomers(){
        return bankAccountService.listCustomers();
    }
}
