package org.asvladimirov.dao.repository;

import org.asvladimirov.dao.entity.ClanEntity;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ClanRepository {

    Optional<ClanEntity> getClanById(long id);

    List<ClanEntity> getAllClans();

    boolean addClan(ClanEntity clanEntity);

    boolean addGoldToClanById(long clanId,int amount, Connection connection);
}
