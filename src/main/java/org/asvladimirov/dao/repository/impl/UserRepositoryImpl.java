package org.asvladimirov.dao.repository.impl;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asvladimirov.dao.entity.ClanEntity;
import org.asvladimirov.dao.entity.UserEntity;
import org.asvladimirov.dao.repository.UserRepository;
import org.asvladimirov.util.ConnectionManager;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@Slf4j
public class UserRepositoryImpl implements UserRepository {


    private static final String SELECT_USER_BY_ID = "SELECT * " +
            "FROM user " +
            "WHERE id = ?";

    private static final String SELECT_ALL_USERS = "SELECT * " +
            "FROM users";

    private static final String SELECT_USER_BY_ID_FOR_UPDATE = "SELECT * " +
            "FROM users " +
            "WHERE id = ? FOR UPDATE";

    private static final String DOWNGRADE_USER_BALANCE = "UPDATE users " +
            "SET balance = balance - ? " +
            "WHERE id = ?";

    private static final String INSERT_NEW_USER = "INSERT INTO users(id,nick_name,balance) " +
            "VALUES (?,?,?)";

    private static final UserRepository INSTANCE = new UserRepositoryImpl();

    @Override
    public Optional<UserEntity> getUserById(long id) {
        log.info("Entered in getUserById , id = {} of UserRepository",id);
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)){
            preparedStatement.setObject(1,id);

            var resultSet = preparedStatement.executeQuery();
            Optional<UserEntity> result =  resultSet.next() ? Optional.of(buildUser(resultSet)) : Optional.empty();
            log.info("Returning {} from getUserById of UserRepository",result);
            return result;
        } catch (SQLException e) {
            log.info("Returning empty value from getUserById , cause {}",e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        log.info("Entered in getAllUsers of UserRepository");
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)){

            var usersResultSet = preparedStatement.executeQuery();
            List<UserEntity> users = new ArrayList<>();
            while (usersResultSet.next()) {
                users.add(buildUser(usersResultSet));
            }
            log.info("Returning {} from getAllUsers method of UserRepository",users);
            return users;
        } catch (SQLException e) {
            log.info("Returning empty list from getAllUsers method of UserRepository, cause {}",e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean addUser(UserEntity userEntity) {
        log.info("Entering in method addUser with args {} of UserRepository",userEntity);
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(INSERT_NEW_USER)){
            preparedStatement.setObject(1,userEntity.getId());
            preparedStatement.setObject(2,userEntity.getNickName());
            preparedStatement.setObject(3,userEntity.getBalance());
            return preparedStatement.execute();
        } catch (SQLException e) {
            log.info("User was not added by addUser , cause {}",e.getMessage());
            return false;
        }
    }

    @Override
    public boolean downgradeBalance(long userId, long clanId, int amount, Connection connection) {
        log.info("Entered in method sendMoneyToClan of UserRepository with args {},{},{}",userId,clanId,amount);
        try (var selectUserStatement = connection.prepareStatement(SELECT_USER_BY_ID_FOR_UPDATE);
        var downGradeBalanceStatement = connection.prepareStatement(DOWNGRADE_USER_BALANCE)){
            selectUserStatement.setObject(1,userId);

            var resultSet = selectUserStatement.executeQuery();
            if (resultSet.next()) {
                var user = buildUser(resultSet);
                if (user.getBalance() >= amount) {
                    downGradeBalanceStatement.setObject(1,amount);
                    downGradeBalanceStatement.setObject(2,userId);
                    downGradeBalanceStatement.executeUpdate();
                    return true;
                }
            }
            return false;

        } catch (SQLException e) {
            log.info("Money transfer from sendMoneyToClan failed, cause {}",e.getMessage());
            System.out.println(e);
            return false;
        }
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    private UserEntity buildUser(ResultSet resultSet) throws SQLException {
        return new UserEntity(resultSet.getObject("id", Long.class),
                resultSet.getObject("nick_name", String.class),
                resultSet.getObject("balance", Integer.class));
    }
}
