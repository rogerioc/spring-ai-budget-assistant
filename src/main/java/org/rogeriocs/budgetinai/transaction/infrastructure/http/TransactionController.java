package org.rogeriocs.budgetinai.transaction.infrastructure.http;

import org.rogeriocs.budgetinai.auth.infrastructure.persistence.entity.User;
import org.rogeriocs.budgetinai.transaction.application.ListTransactionsByCategoryUseCase;
import org.rogeriocs.budgetinai.transaction.application.PersistTransactionUseCase;
import org.rogeriocs.budgetinai.transaction.domain.Category;
import org.rogeriocs.budgetinai.transaction.domain.Owner;
import org.rogeriocs.budgetinai.transaction.infrastructure.http.request.TransactionRequest;
import org.rogeriocs.budgetinai.transaction.infrastructure.http.response.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;

    public TransactionController(PersistTransactionUseCase persistTransactionUseCase,
                                 ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase) {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionsByCategoryUseCase = listTransactionsByCategoryUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @RequestBody TransactionRequest request,
            @AuthenticationPrincipal User user) {
        Owner owner = Optional.ofNullable(user)
                .map(u -> new Owner(u.getId(), u.getUsername()))
                .orElse(null);

        var transaction = persistTransactionUseCase.execute(request.toInput(), owner);
        return TransactionResponse.from(transaction);
    }

    @GetMapping("/{category}")
    public List<TransactionResponse> readTransactions(@PathVariable Category category) {
        return listTransactionsByCategoryUseCase.execute(category)
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }
}
