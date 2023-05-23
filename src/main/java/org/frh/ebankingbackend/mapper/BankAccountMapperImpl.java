package org.frh.ebankingbackend.mapper;

import org.frh.ebankingbackend.dto.CustomerDTO;
import org.frh.ebankingbackend.entity.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();

        // method : 1
        //customerDTO.setId(customer.getId());
        //customerDTO.setName(customer.getName());
        //customerDTO.setEmail(customer.getEmail());

        // method 2
        BeanUtils.copyProperties(customer, customerDTO);

        //method 3
        //MapStruct, JMapper

        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
}
