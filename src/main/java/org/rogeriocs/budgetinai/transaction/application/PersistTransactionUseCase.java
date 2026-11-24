package org.rogeriocs.budgetinai.transaction.application;

import org.rogeriocs.budgetinai.transaction.application.input.PersistTransactionInput;
import org.rogeriocs.budgetinai.transaction.application.output.TransactionOutput;
import org.rogeriocs.budgetinai.transaction.domain.Owner;
import org.rogeriocs.budgetinai.transaction.domain.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class PersistTransactionUseCase {

    private final TransactionRepository transactionRepository;

    public PersistTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionOutput execute(PersistTransactionInput input, Owner owner) {
        var transaction = transactionRepository.save(input.toDomain(owner));
        return TransactionOutput.from(transaction);
    }

    public TransactionOutput execute(PersistTransactionInput input) {
        return execute(input, null);
    }
}
