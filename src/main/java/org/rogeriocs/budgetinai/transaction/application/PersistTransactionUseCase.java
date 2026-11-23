package org.rogeriocs.budgetinai.transaction.application;

import org.rogeriocs.budgetinai.transaction.application.input.PersistTransactionInput;
import org.rogeriocs.budgetinai.transaction.application.output.TransactionOutput;
import org.rogeriocs.budgetinai.transaction.domain.Transaction;
import org.rogeriocs.budgetinai.transaction.domain.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class PersistTransactionUseCase {

    private final TransactionRepository transactionRepository;

    public PersistTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionOutput execute(PersistTransactionInput input) {
        var transaction = transactionRepository.save(
                new Transaction(input.description(), input.amount(), input.category())
        );
        return TransactionOutput.from(transaction);
    }
}
