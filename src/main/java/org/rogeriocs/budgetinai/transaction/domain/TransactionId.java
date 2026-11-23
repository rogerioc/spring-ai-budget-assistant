package org.rogeriocs.budgetinai.transaction.domain;

import java.util.UUID;

public record TransactionId(UUID uuid) {
    public TransactionId() {
        this(UUID.randomUUID());
    }
}
