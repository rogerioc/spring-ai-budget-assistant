package org.rogeriocs.budgetinai.transaction.infrastructure.persistence.repository;

import org.rogeriocs.budgetinai.transaction.domain.Category;
import org.rogeriocs.budgetinai.transaction.domain.Transaction;
import org.rogeriocs.budgetinai.transaction.domain.TransactionRepository;
import org.rogeriocs.budgetinai.transaction.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaTransactionRepository implements TransactionRepository {

    private final TransactionEntityRepository transactionEntityRepository;

    public JpaTransactionRepository(TransactionEntityRepository transactionEntityRepository) {
        this.transactionEntityRepository = transactionEntityRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        var entity = TransactionEntity.from(transaction);
        return transactionEntityRepository.save(entity).toDomain();
    }

    @Override
    public List<Transaction> findAllByCategory(Category category) {
        return transactionEntityRepository.findAllByCategory(category)
                .stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }
}
