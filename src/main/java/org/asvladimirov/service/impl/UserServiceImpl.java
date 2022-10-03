package org.asvladimirov.service.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asvladimirov.dao.repository.ClanRepository;
import org.asvladimirov.dao.repository.UserRepository;
import org.asvladimirov.dao.repository.impl.ClanRepositoryImpl;
import org.asvladimirov.dao.repository.impl.UserRepositoryImpl;
import org.asvladimirov.dto.ReplenishmentType;
import org.asvladimirov.exception.SQLTransactionException;
import org.asvladimirov.service.ClanService;
import org.asvladimirov.service.TransactionHistoryService;
import org.asvladimirov.service.UserService;
import org.asvladimirov.util.ConnectionManager;

import java.sql.SQLException;

import static lombok.AccessLevel.*;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();;
    private final ClanService clanService = ClanServiceImpl.getInstance();
    private final TransactionHistoryService transactionHistoryService = TransactionHistoryServiceImpl.getInstance();

    private static final UserService INSTANCE = new UserServiceImpl();



    @Override
    public boolean sendGoldToClan(long userId, long clanId, int amount) {
        log.info("Entered in sendGoldToClan method with args {},{},{}",userId,clanId,amount);
        var connection = ConnectionManager.get();
        try (connection){
            connection.setAutoCommit(false);
            var downgradeResult = userRepository.downgradeBalance(userId, clanId, amount, connection);
            if (downgradeResult) {
                var addingGoldResult = clanService.addGoldToClan(clanId, amount, connection);
                if (addingGoldResult) {
                    connection.commit();
                    log.info("Successfully sent {} gold from user {} to clan {}",amount,userId,clanId);
                    transactionHistoryService.saveTransactionData(userId,clanId,amount, ReplenishmentType.BALANCE_DOWNGRADE);
                    return true;
                }
            }
            connection.rollback();
            log.info("Unsuccessfully, didn`t send {} gold from user {} to clan {}",amount,userId,clanId);
            return false;

        } catch (SQLException e) {
            log.info("Unsuccessfully , gold wasn`t send , cause {}",e.getMessage());
            throw new SQLTransactionException(e.getMessage());
        }
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
