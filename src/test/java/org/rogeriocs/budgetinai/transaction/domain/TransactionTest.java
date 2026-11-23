package org.rogeriocs.budgetinai.transaction.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {

    @Test
    void shouldGenerateIdWhenCreatedWithoutExplicitId() {
        var transaction = new Transaction("Supermercado", 15000L, Category.GROCERIES);

        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getId().uuid()).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("Supermercado");
        assertThat(transaction.getAmount()).isEqualTo(15000L);
        assertThat(transaction.getCategory()).isEqualTo(Category.GROCERIES);
    }

    @Test
    void shouldPreserveExplicitIdWhenProvided() {
        var uuid = UUID.randomUUID();
        var id = new TransactionId(uuid);
        var transaction = new Transaction(id, "Farmácia", 4500L, Category.PHARMA);

        assertThat(transaction.getId()).isEqualTo(id);
        assertThat(transaction.getId().uuid()).isEqualTo(uuid);
        assertThat(transaction.getDescription()).isEqualTo("Farmácia");
        assertThat(transaction.getAmount()).isEqualTo(4500L);
        assertThat(transaction.getCategory()).isEqualTo(Category.PHARMA);
    }
}
