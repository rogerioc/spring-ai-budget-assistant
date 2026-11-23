package org.rogeriocs.budgetinai.transaction.infrastructure.http.request;

import org.rogeriocs.budgetinai.transaction.application.input.PersistTransactionInput;
import org.rogeriocs.budgetinai.transaction.domain.Category;

public record TransactionRequest(String description, Category category, long amount) {

    public PersistTransactionInput toInput() {
        return new PersistTransactionInput(description, amount, category);
    }
}
