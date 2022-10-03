package org.asvladimirov.dao.repository;

import org.asvladimirov.dao.entity.UserEntity;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> getUserById(long id);

    List<UserEntity> getAllUsers();

    boolean addUser(UserEntity userEntity);

    boolean downgradeBalance(long userId, long clanId, int amount, Connection connection);
}
