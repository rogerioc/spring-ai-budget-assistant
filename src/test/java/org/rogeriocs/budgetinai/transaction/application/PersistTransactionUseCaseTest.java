package org.rogeriocs.budgetinai.transaction.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rogeriocs.budgetinai.transaction.application.input.PersistTransactionInput;
import org.rogeriocs.budgetinai.transaction.domain.Category;
import org.rogeriocs.budgetinai.transaction.domain.Transaction;
import org.rogeriocs.budgetinai.transaction.domain.TransactionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersistTransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PersistTransactionUseCase useCase;

    @Test
    void shouldPersistTransactionAndReturnOutput() {
        var input = new PersistTransactionInput("Combustível", 20000L, Category.AUTO);
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var output = useCase.execute(input);

        var captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        var saved = captor.getValue();
        assertThat(saved.getDescription()).isEqualTo("Combustível");
        assertThat(saved.getAmount()).isEqualTo(20000L);
        assertThat(saved.getCategory()).isEqualTo(Category.AUTO);

        assertThat(output.id()).isEqualTo(saved.getId().uuid().toString());
        assertThat(output.description()).isEqualTo("Combustível");
        assertThat(output.category()).isEqualTo("AUTO");
        assertThat(output.value()).isEqualTo(20000.0);
    }
}
