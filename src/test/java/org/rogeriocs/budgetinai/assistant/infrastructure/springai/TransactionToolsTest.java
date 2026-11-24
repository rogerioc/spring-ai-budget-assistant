package org.rogeriocs.budgetinai.assistant.infrastructure.springai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rogeriocs.budgetinai.transaction.application.ListTransactionsByCategoryUseCase;
import org.rogeriocs.budgetinai.transaction.application.PersistTransactionUseCase;
import org.rogeriocs.budgetinai.transaction.application.input.PersistTransactionInput;
import org.rogeriocs.budgetinai.transaction.application.output.TransactionOutput;
import org.rogeriocs.budgetinai.transaction.domain.Category;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionToolsTest {

    @Mock
    private PersistTransactionUseCase persistTransactionUseCase;

    @Mock
    private ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;

    @InjectMocks
    private TransactionTools transactionTools;

    @Test
    void shouldDelegatePersistTransactionTool() {
        var expectedInput = new PersistTransactionInput("Padaria", 2500L, Category.GROCERIES);
        var expectedOutput = new TransactionOutput("id-1", "Padaria", "GROCERIES", 2500.0);
        when(persistTransactionUseCase.execute(expectedInput, null)).thenReturn(expectedOutput);

        var result = transactionTools.persistTransaction("Padaria", 2500L, Category.GROCERIES);

        assertThat(result).isEqualTo(expectedOutput);
        verify(persistTransactionUseCase).execute(expectedInput, null);
    }

    @Test
    void shouldDelegateListTransactionsByCategoryTool() {
        var expectedOutput = List.of(new TransactionOutput("id-1", "Remédio", "PHARMA", 5000.0));
        when(listTransactionsByCategoryUseCase.execute(Category.PHARMA)).thenReturn(expectedOutput);

        var result = transactionTools.listTransactionsByCategory(Category.PHARMA);

        assertThat(result).isEqualTo(expectedOutput);
        verify(listTransactionsByCategoryUseCase).execute(Category.PHARMA);
    }
}
