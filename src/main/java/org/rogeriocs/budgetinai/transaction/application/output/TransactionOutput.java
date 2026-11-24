package org.rogeriocs.budgetinai.transaction.application.output;

import org.rogeriocs.budgetinai.transaction.domain.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public record TransactionOutput(
        String id,
        String description,
        String category,
        double value,
        String ownerId,
        String ownerName
) {

    public TransactionOutput(String id, String description, String category, double value) {
        this(id, description, category, value, null, null);
    }

    public static TransactionOutput from(Transaction transaction) {
        var owner = Optional.ofNullable(transaction.getOwner());
        return new TransactionOutput(
                transaction.getId().uuid().toString(),
                transaction.getDescription(),
                transaction.getCategory().name(),
                BigDecimal.valueOf(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                owner.map(o -> o.id().toString()).orElse(null),
                owner.map(o -> o.name()).orElse(null)
        );
    }
}
