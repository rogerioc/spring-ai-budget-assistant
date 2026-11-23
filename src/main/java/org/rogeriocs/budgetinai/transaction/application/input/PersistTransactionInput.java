package org.rogeriocs.budgetinai.transaction.application.input;

import org.rogeriocs.budgetinai.transaction.domain.Category;

public record PersistTransactionInput(
        String description,
        long amount,
        Category category
) {
}
