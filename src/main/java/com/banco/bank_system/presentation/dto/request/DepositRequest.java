package com.banco.bank_system.presentation.dto.request;

public record DepositRequest(String branch, String accountNumber, String value){
}
