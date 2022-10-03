package org.asvladimirov.service;

public interface UserService {

    boolean sendGoldToClan(long userId,long clanId,int amount);
}
