package com.km.cryptocurrencies.controller;

import com.km.cryptocurrencies.dto.CryptoCurrencyDto;
import com.km.cryptocurrencies.service.CurrencyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/api")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(path = "/current")
    public @ResponseBody Map getCurrentValues() throws URISyntaxException, IOException, InterruptedException {
        return currencyService.getCurrentValues();
    }

    @GetMapping(path = "/history")
    public @ResponseBody List<CryptoCurrencyDto> getAllHistoryValues() {
        return currencyService.getAllHistoryValues();
    }

    @GetMapping(path = "/history/{coinId}")
    public @ResponseBody List<CryptoCurrencyDto> getHistoryValuesForCurrency(@PathVariable String coinId) {
        return currencyService.getHistoryValuesForCoinId(coinId);
    }

}