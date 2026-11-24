package org.rogeriocs.budgetinai.transaction.application.input;

import org.rogeriocs.budgetinai.transaction.domain.Category;
import org.rogeriocs.budgetinai.transaction.domain.Owner;
import org.rogeriocs.budgetinai.transaction.domain.Transaction;

public record PersistTransactionInput(
        String description,
        long amount,
        Category category
) {
    public Transaction toDomain(Owner owner) {
        return new Transaction(description, amount, category, owner);
    }
}
