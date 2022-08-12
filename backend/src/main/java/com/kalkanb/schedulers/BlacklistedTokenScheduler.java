package com.kalkanb.schedulers;

import com.kalkanb.entity.BlacklistedTokenEntity;
import com.kalkanb.exception.ProjectException;
import com.kalkanb.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BlacklistedTokenScheduler {

    private BlacklistedTokenRepository blacklistedTokenRepository;

    //every minute at second 0
    //lock method for 1 minute after each execution
    @Scheduled(cron = "0 */1 * * * *")
    @SchedulerLock(name = "expiredBlacklistedTokenDeletionScheduler",
            lockAtLeastFor = "PT1M",
            lockAtMostFor = "PT1M")
    public void expiredBlacklistedTokenDeletionScheduler() {
        try {
            List<BlacklistedTokenEntity> expiredTokens =
                    blacklistedTokenRepository.findAllByExpirationDateBefore(LocalDateTime.now());
            if (CollectionUtils.isNotEmpty(expiredTokens)) {
                blacklistedTokenRepository.deleteAll(expiredTokens);
            }
        } catch (Exception e) {
            throw new ProjectException(e.getMessage(), e);
        }
    }

    @Autowired
    public void setBlacklistedTokenRepository(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }
}
