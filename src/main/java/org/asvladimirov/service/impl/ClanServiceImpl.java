package org.asvladimirov.service.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asvladimirov.dao.repository.ClanRepository;
import org.asvladimirov.dao.repository.impl.ClanRepositoryImpl;
import org.asvladimirov.exception.SQLTransactionException;
import org.asvladimirov.service.ClanService;
import org.asvladimirov.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class ClanServiceImpl implements ClanService {
    private final ClanRepository clanRepository = ClanRepositoryImpl.getInstance();

    private static final ClanService INSTANCE = new ClanServiceImpl();
    
    @Override
    public boolean addGoldToClan(long clanId, int amount) {
        var connection = ConnectionManager.get();
        var addingResult = clanRepository.addGoldToClanById(clanId, amount, connection);
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SQLTransactionException(e.getMessage());
        }
        return addingResult;
    }

    @Override
    public boolean addGoldToClan(long clanId, int amount, Connection connection) {
        return clanRepository.addGoldToClanById(clanId,amount,connection);
    }

    public static ClanService getInstance() {
        return INSTANCE;
    }
}
