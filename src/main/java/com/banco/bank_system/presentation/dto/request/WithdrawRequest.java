package com.banco.bank_system.presentation.dto.request;

public record WithdrawRequest(String branch, String accountNumber, String amount){}
