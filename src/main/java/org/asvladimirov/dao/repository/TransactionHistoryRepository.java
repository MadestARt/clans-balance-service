package org.asvladimirov.dao.repository;

import org.asvladimirov.dto.ReplenishmentType;

public interface TransactionHistoryRepository {

    boolean saveTransactionData(long userId, long clanId, long amount, ReplenishmentType replenishmentType);
}
