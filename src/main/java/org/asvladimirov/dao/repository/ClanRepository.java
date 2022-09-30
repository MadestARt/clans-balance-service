package org.asvladimirov.dao.repository;

import org.asvladimirov.dao.entity.ClanEntity;

import java.util.List;
import java.util.Optional;

public interface ClanRepository {

    Optional<ClanEntity> getClanById(long id);

    List<ClanEntity> getAllClans();

    boolean addNewClan(ClanEntity clanEntity);
}
