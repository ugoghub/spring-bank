package com.banco.bank_system.presentation.dto.request.transactions;

public record TransferRequest(String fromBranch,
                              String fromAccountNumber,
                              String toBranch,
                              String toAccountNumber,
                              String amount){
}
