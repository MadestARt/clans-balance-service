package org.asvladimirov.service;

import org.asvladimirov.dto.ReplenishmentType;

public interface TransactionHistoryService {

    boolean saveTransactionData(long userId, long clanId, long amount, ReplenishmentType replenishmentType);
}
