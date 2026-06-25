package com.banco.bank_system.infrastructure.database.entities;


import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(
        name = "tb_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "account_number",
                                "agency_number"
                        }
                )
        }
)
@Getter
@NoArgsConstructor
public class AccountEntity {


    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;


    @Column(
            name = "client_id",
            nullable = false
    )
    private UUID clientId;


    @Column(
            name = "account_number",
            nullable = false
    )
    private String accountNumber;


    @Column(
            name = "agency_number",
            nullable = false
    )
    private String branch;


    @Column(nullable = false)
    private BigDecimal balance;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "account_type",
            nullable = false
    )
    private AccountType accountType;


    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;



    public AccountEntity(
            UUID id,
            UUID clientId,
            String accountNumber,
            String branch,
            BigDecimal balance,
            AccountType accountType,
            LocalDateTime createdAt
    ){

        this.id = id;
        this.clientId = clientId;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = createdAt;
    }



    public Account toDomain(){

        AccountIdentity identity =
                new AccountIdentity(
                        branch,
                        accountNumber
                );


        Money money =
                Money.of(balance);



        return switch (accountType){

            case CHECKING ->
                    new CheckingAccount(
                            id,
                            clientId,
                            identity,
                            money,
                            createdAt
                    );


            case SAVINGS ->
                    new SavingsAccount(
                            id,
                            clientId,
                            identity,
                            money,
                            createdAt
                    );
        };

    }



    public static AccountEntity fromDomain(Account account){


        AccountType type =
                account instanceof CheckingAccount
                        ? AccountType.CHECKING
                        : AccountType.SAVINGS;



        return new AccountEntity(

                account.getId(),

                account.getClientId(),

                account.getAccountIdentity()
                        .accountNumber(),

                account.getAccountIdentity()
                        .branch(),

                account.getBalance()
                        .value(),

                type,

                account.getCreationTime()

        );

    }

}