package com.banco.bank_system.domain.valueobject;

import java.util.concurrent.ThreadLocalRandom;

public final class AccountIdentityFactory {

    private static final int ACCOUNT_NUMBER_LIMIT = 1_000_000;
    private static final int BRANCH_LIMIT = 50;

    private AccountIdentityFactory() {
    }

    private static String generateBranch() {
        int branch = ThreadLocalRandom.current().nextInt(0, ACCOUNT_NUMBER_LIMIT);

        return String.format("%02d", branch);
    }

    private static String generateAccountNumber() {
        String accountNumber = String.format("%06d",
                ThreadLocalRandom.current().nextInt(0, BRANCH_LIMIT));

        return accountNumber + "-" + generateDigit(accountNumber);
    }

    private static int generateDigit(String accountNumber) {
        int sum = 0;

        for(char c : accountNumber.toCharArray()) {
            sum += Character.getNumericValue(c);
        }

        return sum % 10;
    }

    public static AccountIdentity generate(){
        return new AccountIdentity(generateBranch(), generateAccountNumber());
    }
}
