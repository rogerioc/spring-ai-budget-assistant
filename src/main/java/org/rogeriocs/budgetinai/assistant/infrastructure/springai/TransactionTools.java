package org.rogeriocs.budgetinai.assistant.infrastructure.springai;

import org.rogeriocs.budgetinai.auth.infrastructure.persistence.entity.User;
import org.rogeriocs.budgetinai.transaction.application.ListTransactionsByCategoryUseCase;
import org.rogeriocs.budgetinai.transaction.application.PersistTransactionUseCase;
import org.rogeriocs.budgetinai.transaction.application.input.PersistTransactionInput;
import org.rogeriocs.budgetinai.transaction.application.output.TransactionOutput;
import org.rogeriocs.budgetinai.transaction.domain.Category;
import org.rogeriocs.budgetinai.transaction.domain.Owner;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
        Owner owner = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .map(u -> new Owner(u.getId(), u.getUsername()))
                .orElse(null);

        return persistTransactionUseCase.execute(new PersistTransactionInput(description, amount, category), owner);
    }

    @Tool(name = "list-transaction-by-category", description = "Lista transações financeiras por categoria")
    public List<TransactionOutput> listTransactionsByCategory(
            @ToolParam(description = "Categoria da transação") Category category) {
        return listTransactionsByCategoryUseCase.execute(category);
    }
}
