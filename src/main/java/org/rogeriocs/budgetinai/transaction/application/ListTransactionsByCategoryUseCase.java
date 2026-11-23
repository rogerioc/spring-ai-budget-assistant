package org.rogeriocs.budgetinai.transaction.application;

import org.rogeriocs.budgetinai.transaction.application.output.TransactionOutput;
import org.rogeriocs.budgetinai.transaction.domain.Category;
import org.rogeriocs.budgetinai.transaction.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListTransactionsByCategoryUseCase {

    private final TransactionRepository transactionRepository;

    public ListTransactionsByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionOutput> execute(Category category) {
        return transactionRepository.findAllByCategory(category)
                .stream()
                .map(TransactionOutput::from)
                .toList();
    }
}
