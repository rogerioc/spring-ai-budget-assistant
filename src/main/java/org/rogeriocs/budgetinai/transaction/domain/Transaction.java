package org.rogeriocs.budgetinai.transaction.domain;

import lombok.Getter;

@Getter
public class Transaction {
    private final TransactionId id;
    private final String description;
    private final long amount;
    private final Category category;
    private final Owner owner;

    public Transaction(String description, long amount, Category category) {
        this(new TransactionId(), description, amount, category, null);
    }

    public Transaction(String description, long amount, Category category, Owner owner) {
        this(new TransactionId(), description, amount, category, owner);
    }

    public Transaction(TransactionId id, String description, long amount, Category category) {
        this(id, description, amount, category, null);
    }

    public Transaction(TransactionId id, String description, long amount, Category category, Owner owner) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.owner = owner;
    }
}
