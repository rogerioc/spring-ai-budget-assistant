package org.rogeriocs.budgetinai.transaction.infrastructure.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rogeriocs.budgetinai.transaction.application.output.TransactionOutput;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionResponse(
        String id,
        String category,
        String description,
        double amount,
        OwnerResponse owner
) {

    public record OwnerResponse(String id, String name) {
    }

    public TransactionResponse(String id, String category, String description, double amount) {
        this(id, category, description, amount, null);
    }

    public static TransactionResponse from(TransactionOutput output) {
        OwnerResponse owner = (output.ownerId() != null || output.ownerName() != null)
                ? new OwnerResponse(output.ownerId(), output.ownerName())
                : null;
        return new TransactionResponse(
                output.id(),
                output.category(),
                output.description(),
                output.value(),
                owner
        );
    }
}
