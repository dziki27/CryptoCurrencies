package com.km.cryptocurrencies.scheduler;

import com.km.cryptocurrencies.service.CurrencyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfiguration {

    @Bean
    public SchedulingJob scheduleJob(CurrencyService currencyService) {
        return new SchedulingJob(currencyService);
    }
}