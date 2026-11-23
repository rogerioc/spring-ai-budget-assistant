package org.rogeriocs.budgetinai.assistant.infrastructure.springai;

import org.rogeriocs.budgetinai.transaction.application.ListTransactionsByCategoryUseCase;
import org.rogeriocs.budgetinai.transaction.application.PersistTransactionUseCase;
import org.rogeriocs.budgetinai.transaction.application.input.PersistTransactionInput;
import org.rogeriocs.budgetinai.transaction.application.output.TransactionOutput;
import org.rogeriocs.budgetinai.transaction.domain.Category;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionTools {

    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;

    public TransactionTools(PersistTransactionUseCase persistTransactionUseCase,
                            ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase) {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionsByCategoryUseCase = listTransactionsByCategoryUseCase;
    }

    @Tool(name = "persist-transaction", description = "Persiste uma nova transação financeira")
    public TransactionOutput persistTransaction(
            @ToolParam(description = "Descrição do gasto") String description,
            @ToolParam(description = "Valor do gasto (em centavos)") long amount,
            @ToolParam(description = "Categoria de uma transação") Category category) {
        return persistTransactionUseCase.execute(new PersistTransactionInput(description, amount, category));
    }

    @Tool(name = "list-transaction-by-category", description = "Lista transações financeiras por categoria")
    public List<TransactionOutput> listTransactionsByCategory(
            @ToolParam(description = "Categoria da transação") Category category) {
        return listTransactionsByCategoryUseCase.execute(category);
    }
}
