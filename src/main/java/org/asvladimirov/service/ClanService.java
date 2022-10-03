package org.asvladimirov.service;

import java.sql.Connection;

public interface ClanService {

    boolean addGoldToClan(long clanId,int amount);

    boolean addGoldToClan(long clanId, int amount, Connection connection);
}
