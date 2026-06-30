package com.banco.bank_system.presentation.dto.request;

public record TransferRequest(String fromBranch,
                              String fromAccountNumber,
                              String toBranch,
                              String toAccountNumber,
                              String amount){
}
