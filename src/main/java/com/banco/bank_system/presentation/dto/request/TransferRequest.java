package com.banco.bank_system.presentation.dto.request;

public record TransferRequest(String branch, String accountNumber, String toBranch, String toAccountNumber, String value){
}
