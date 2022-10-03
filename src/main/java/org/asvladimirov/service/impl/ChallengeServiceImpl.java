package org.asvladimirov.service.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asvladimirov.dto.ReplenishmentType;
import org.asvladimirov.service.ChallengeService;
import org.asvladimirov.service.ClanService;
import org.asvladimirov.service.TransactionHistoryService;

import static java.lang.Math.*;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class ChallengeServiceImpl implements ChallengeService {
    private final ClanService clanService = ClanServiceImpl.getInstance();
    private final TransactionHistoryService transactionHistoryService = TransactionHistoryServiceImpl.getInstance();

    private static final ChallengeService INSTANCE = new ChallengeServiceImpl();

    @Override
    public boolean startChallenge(long userId,long challengeId, long clanId) {
        log.info("Entered in method startChallenge with args {},{},{}",userId,challengeId,clanId);
        var challengeBounty = calculateChallengeBounty(challengeId);
        if (random() > 0.5) {
            var clanBalanceResult = clanService.addGoldToClan(clanId, challengeBounty);
            if (clanBalanceResult) {
                transactionHistoryService.saveTransactionData(userId, clanId, challengeBounty, ReplenishmentType.CHALLENGE);
                log.info("Successfully , sent {} gold to clan {} by completing challenge {} by user {}",challengeBounty,clanId,challengeId,userId);
                return true;
            }
        }
        return false;
    }

    private int calculateChallengeBounty(long challengeId) {
        return (int) (10 * challengeId);
    }

    public static ChallengeService getInstance() {
        return INSTANCE;
    }
}
