package com.blog.config;

import com.blog.common.CacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTask {
    @Scheduled(fixedRate = 60000)
    public void fixedRateTask() {
        log.info("fixedRateTask 1 min");
    }
}
