package org.asvladimirov.dao.repository.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asvladimirov.dao.repository.TransactionHistoryRepository;
import org.asvladimirov.dao.repository.UserRepository;
import org.asvladimirov.dto.ReplenishmentType;
import org.asvladimirov.util.ConnectionManager;

import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class TransactionHistoryRepositoryImpl implements TransactionHistoryRepository {

    private static final TransactionHistoryRepository INSTANCE = new TransactionHistoryRepositoryImpl();
    private static final String INSERT_TRANSACTION_DATA = "INSERT INTO transaction_history (user_id,clan_id,amount,replenishment_type) " +
            "VALUES (?,?,?,?)";

    @Override
    public boolean saveTransactionData(long userId, long clanId, long amount, ReplenishmentType replenishmentType) {
        log.info("Entered in saveTransactionData with args {},{},{},{}",userId,clanId,amount,replenishmentType);
        try (var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(INSERT_TRANSACTION_DATA)){
            preparedStatement.setObject(1,userId);
            preparedStatement.setObject(2,clanId);
            preparedStatement.setObject(3,amount);
            preparedStatement.setObject(4,replenishmentType.getDescription());
            var updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                log.info("Successfully saved transaction data : {},{},{},{}",userId,clanId,amount,replenishmentType);
                return true;
            }
            log.info("Unsuccessfully , didn`t save transaction data {},{},{},{}",userId,clanId,amount,replenishmentType);
            return false;
        } catch (SQLException e) {
            log.info("Unsuccessfully , didn`t save transaction data {},{},{},{}",userId,clanId,amount,replenishmentType);
            return false;
        }
    }

    public static TransactionHistoryRepository getInstance() {
        return INSTANCE;
    }
}
