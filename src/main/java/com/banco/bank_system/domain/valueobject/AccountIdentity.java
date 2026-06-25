package com.banco.bank_system.domain.valueobject;

public record AccountIdentity(String branch, String accountNumber) {

    public AccountIdentity {

        if (branch == null) {
            throw new IllegalArgumentException("Agência inválida");
        }

        if (accountNumber == null) {
            throw new IllegalArgumentException("Número da conta inválido");
        }

        branch = branch.trim();
        accountNumber = accountNumber.trim();

        if (branch.isBlank() || !branch.matches("\\d{2}")) {
            throw new IllegalArgumentException("Agência inválida");
        }

        String digits = accountNumber.replace("-", "");

        if (!accountNumber.matches("\\d{6}-\\d")
                || !isValidDigit(digits)) {

            throw new IllegalArgumentException("Número da conta inválido");
        }
    }

    private static boolean isValidDigit(String accountNumber){
        int digit = accountNumber.charAt(accountNumber.length() - 1) - '0';

        int sum = 0;

        for(int i = 0; i < accountNumber.length()-1; i++){
            sum += Character.getNumericValue(accountNumber.charAt(i));
        }

        int expectedDigit = sum % 10;

        return expectedDigit == digit;
    }
}
