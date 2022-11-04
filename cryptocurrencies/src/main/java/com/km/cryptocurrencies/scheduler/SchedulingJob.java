package com.km.cryptocurrencies.scheduler;

import com.km.cryptocurrencies.service.CurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.URISyntaxException;

@AllArgsConstructor
@Slf4j
public class SchedulingJob {

    private final CurrencyService currencyService;

    @Scheduled(fixedRate = 30000)
    public void getCurrentValues() throws URISyntaxException, IOException {
        try {
            currencyService.saveCurrencies();
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
        }
    }
}
