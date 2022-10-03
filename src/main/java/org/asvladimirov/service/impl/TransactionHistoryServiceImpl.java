package org.asvladimirov.service.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asvladimirov.dao.repository.TransactionHistoryRepository;
import org.asvladimirov.dao.repository.impl.TransactionHistoryRepositoryImpl;
import org.asvladimirov.dto.ReplenishmentType;
import org.asvladimirov.service.TransactionHistoryService;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository = TransactionHistoryRepositoryImpl.getInstance();

    private static final TransactionHistoryService INSTANCE = new TransactionHistoryServiceImpl();

    @Override
    public boolean saveTransactionData(long userId, long clanId, long amount, ReplenishmentType replenishmentType) {
        return transactionHistoryRepository.saveTransactionData(userId,clanId,amount,replenishmentType);
    }

    public static TransactionHistoryService getInstance() {
        return INSTANCE;
    }
}
