package org.frh.ebankingbackend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frh.ebankingbackend.entity.BankAccount;

import java.util.List;

// for gettes/setters

@Data
public class CustomerDTO {

    private Long id;
    //private String name;
    private String email;

}
