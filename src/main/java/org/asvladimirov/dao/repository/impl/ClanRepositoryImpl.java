package org.asvladimirov.dao.repository.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asvladimirov.dao.entity.ClanEntity;
import org.asvladimirov.dao.repository.ClanRepository;
import org.asvladimirov.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
@Slf4j
public class ClanRepositoryImpl implements ClanRepository {

    private static final ClanRepository CLAN_REPOSITORY_INSTANCE = new ClanRepositoryImpl();

    private static final String SELECT_ALL_CLANS = "SELECT * FROM clan";

    private static final String SELECT_CLAN_BY_ID = "SELECT * FROM clan " +
            "WHERE id = ?";

    private static final String INSERT_NEW_CLAN = "INSERT INTO clan(id,name,gold,members_amount) " +
            "VALUES (?,?,?,?)";

    private static final String ADD_BALANCE_TO_CLAN = "UPDATE clan " +
            "SET gold = gold + ? " +
            "WHERE id = ?";


    @Override
    public Optional<ClanEntity> getClanById(long id) {
        log.info("Entered in getClanById , id = {} of ClanRepository",id);
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SELECT_CLAN_BY_ID)){
            preparedStatement.setObject(1,id);

            var resultSet = preparedStatement.executeQuery();
            Optional<ClanEntity> result =  resultSet.next() ? Optional.of(buildClan(resultSet)) : Optional.empty();
            log.info("Returning {} from getClanById of ClanRepository",result);
            return result;
        } catch (SQLException e) {
            log.info("Returning empty value from getClanById , cause {}",e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<ClanEntity> getAllClans() {
        log.info("Entered in getAllClans of ClanRepository");
        try (var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(SELECT_ALL_CLANS)){

            var clansResultSet = preparedStatement.executeQuery();
            List<ClanEntity> clans = new ArrayList<>();
            while (clansResultSet.next()) {
                clans.add(buildClan(clansResultSet));
            }
            log.info("Returning {} from getAllClans method of ClanRepository",clans);
            return clans;
        } catch (SQLException e) {
            log.info("Returning empty list from getAllClans method of ClanRepository, cause {}",e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean addClan(ClanEntity clanEntity) {
        log.info("Entering in method addNewClan with args {} of ClanRepository",clanEntity);
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(INSERT_NEW_CLAN)){
            preparedStatement.setObject(1,clanEntity.getId());
            preparedStatement.setObject(2,clanEntity.getName());
            preparedStatement.setObject(3,clanEntity.getGold());
            preparedStatement.setObject(4,clanEntity.getMembersAmount());
            return preparedStatement.execute();
        } catch (SQLException e) {
            log.info("Clan was not added by addNewClan , cause {}",e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addGoldToClanById(long clanId,int amount,Connection connection) {
        try (
             var preparedStatement = connection.prepareStatement(ADD_BALANCE_TO_CLAN)){
            log.info("Entered in method addGoldToClanById with args {},{},{} of ClanRepository",clanId,amount,connection);
            preparedStatement.setObject(1,amount);
            preparedStatement.setObject(2,clanId);

            var updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                log.info("Successfully ,added {} gold to clan with id {}",amount,clanId);
                return true;
            } else {
                log.info("Unsuccessfully,  not added {} gold to clan with id {}",amount,clanId);
                return false;
            }
        } catch (SQLException e) {
            log.info("Unsuccessfully, not added {} gold to clan with id {}",amount,clanId);
            return false;
        }
    }



    public static ClanRepository getInstance() {
        return CLAN_REPOSITORY_INSTANCE;
    }

    private ClanEntity buildClan(ResultSet clansResultSet) throws SQLException{
        return new ClanEntity(clansResultSet.getObject("id",Long.class),
                clansResultSet.getObject("name",String.class),
                clansResultSet.getObject("gold", Integer.class),
                clansResultSet.getObject("members_amount", Short.class));
    }
}
