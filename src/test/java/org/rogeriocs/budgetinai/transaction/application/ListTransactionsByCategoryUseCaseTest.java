package org.rogeriocs.budgetinai.transaction.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rogeriocs.budgetinai.transaction.domain.Category;
import org.rogeriocs.budgetinai.transaction.domain.Transaction;
import org.rogeriocs.budgetinai.transaction.domain.TransactionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListTransactionsByCategoryUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ListTransactionsByCategoryUseCase useCase;

    @Test
    void shouldReturnMappedTransactionsForCategory() {
        var t1 = new Transaction("Mercado A", 8000L, Category.GROCERIES);
        var t2 = new Transaction("Mercado B", 12000L, Category.GROCERIES);
        when(transactionRepository.findAllByCategory(Category.GROCERIES)).thenReturn(List.of(t1, t2));

        var result = useCase.execute(Category.GROCERIES);

        verify(transactionRepository).findAllByCategory(Category.GROCERIES);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).description()).isEqualTo("Mercado A");
        assertThat(result.get(0).category()).isEqualTo("GROCERIES");
        assertThat(result.get(1).description()).isEqualTo("Mercado B");
    }

    @Test
    void shouldReturnEmptyListWhenNoTransactionsFound() {
        when(transactionRepository.findAllByCategory(Category.PHARMA)).thenReturn(List.of());

        var result = useCase.execute(Category.PHARMA);

        assertThat(result).isEmpty();
    }
}
