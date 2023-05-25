package org.frh.ebankingbackend.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frh.ebankingbackend.entity.BankAccount;
import org.frh.ebankingbackend.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date date;
    private double amount;
    private OperationType type;
    private String description;
}

