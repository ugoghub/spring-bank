package com.banco.bank_system.presentation.dto.request.transactions;

public record DepositRequest(String branch,
                             String accountNumber,
                             String amount){
}
