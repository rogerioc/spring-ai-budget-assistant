package org.rogeriocs.budgetinai.transaction.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rogeriocs.budgetinai.transaction.domain.Category;
import org.rogeriocs.budgetinai.transaction.domain.Transaction;
import org.rogeriocs.budgetinai.transaction.domain.TransactionId;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    private UUID id;
    private String description;
    private long amount;
    @Enumerated(EnumType.STRING)
    private Category category;

    public static TransactionEntity from(Transaction transaction) {
        return new TransactionEntity(
                transaction.getId().uuid(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getCategory()
        );
    }

    public Transaction toDomain() {
        return new Transaction(
                new TransactionId(this.id),
                this.description,
                this.amount,
                this.category
        );
    }
}
