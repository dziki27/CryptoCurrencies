package com.km.cryptocurrencies.service;

import com.km.cryptocurrencies.dto.CoinDto;
import com.km.cryptocurrencies.dto.CryptoCurrencyDto;
import com.km.cryptocurrencies.dto.ValueWithDateDto;
import com.km.cryptocurrencies.dto.VsCurrenciesWithData;
import com.km.cryptocurrencies.entity.CryptoCurrency;
import com.km.cryptocurrencies.entity.CurrencyValue;
import com.km.cryptocurrencies.repository.CurrencyRepository;
import com.km.cryptocurrencies.repository.CurrencyValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final IntegrationService integrationService;
    private final CurrencyRepository currencyRepository;
    private final CurrencyValueRepository currencyValueRepository;

    @Transactional
    public void saveCurrencies() throws URISyntaxException, IOException, InterruptedException {
        List<String> ids = getCryptoCurrenciesIds();
        List<String> vsCurrencies = getVsCurrencies();

        var prices = getCurrenciesFromExchange(ids, vsCurrencies);

        ids.forEach(id -> vsCurrencies.forEach(vsCurrency -> currencyValueRepository.save(createCurrencyValue(id, (Map<String, Object>) prices.get(id), vsCurrency))));
    }

    public Map<String, Object> getCurrentValues() throws URISyntaxException, IOException, InterruptedException {
        return getCurrenciesFromExchange(getCryptoCurrenciesIds(), getVsCurrencies());
    }

    public List<CryptoCurrencyDto> getAllHistoryValues() {
        return getCryptoCurrencyWithValueDtos(currencyRepository.findAllHistory());
    }

    public List<CryptoCurrencyDto> getHistoryValuesForCoinId(String coinId) {
        return getCryptoCurrencyWithValueDtos(currencyRepository.findAllHistory(coinId));
    }

    private List<CryptoCurrencyDto> getCryptoCurrencyWithValueDtos(List<CryptoCurrencyDto> currencyForCoinId) {
        currencyForCoinId.forEach(currency -> currencyValueRepository.findVsCurrenciesWithData(currency.getId()).forEach(vsCurrencyWithValue -> fillValues(currency, vsCurrencyWithValue)));
        return currencyForCoinId;
    }

    private static void fillValues(CryptoCurrencyDto currency, VsCurrenciesWithData vsCurrencyWithValue) {
        String vsCurrency = vsCurrencyWithValue.getVsCurrency();

        if (!currency.getValues().containsKey(vsCurrency)) {
            List<ValueWithDateDto> list = new ArrayList<>();
            list.add(createValueWithDate(vsCurrencyWithValue));
            currency.getValues().put(vsCurrency, list);
        } else {
            currency.getValues().get(vsCurrency).add(createValueWithDate(vsCurrencyWithValue));
        }
    }

    private static ValueWithDateDto createValueWithDate(VsCurrenciesWithData vsCurrencyWithValue) {
        return ValueWithDateDto.builder().value(vsCurrencyWithValue.getValue()).date(vsCurrencyWithValue.getCreationTime()).build();
    }

    private CurrencyValue createCurrencyValue(String id, Map<String, Object> values, String vsCurrency) {
        return CurrencyValue.builder().currency(vsCurrency).value(Double.valueOf(values.get(vsCurrency).toString())).cryptoCurrency(getCryptoCurrency(id)).build();
    }

    private CryptoCurrency getCryptoCurrency(String id) {
        Optional<CryptoCurrency> cryptoCurrencyOptional = currencyRepository.findByCoinId(id);
        return cryptoCurrencyOptional.isEmpty() ? currencyRepository.save(CryptoCurrency.builder().coinId(id).build()) : cryptoCurrencyOptional.get();
    }

    private Map<String, Object> getCurrenciesFromExchange(List<String> ids, List<String> vsCurrencies) throws URISyntaxException, IOException, InterruptedException {
        return integrationService.getPricesToHashMap("/simple/price?ids=" + String.join(",", ids) + "&vs_currencies=" + String.join(",", vsCurrencies));
    }

    private List<String> getVsCurrencies() throws URISyntaxException, IOException, InterruptedException {
        return integrationService.getListFromService(String.class, "/simple/supported_vs_currencies");
    }

    private List<String> getCryptoCurrenciesIds() throws URISyntaxException, IOException, InterruptedException {
        return integrationService.getListFromService(CoinDto.class, "/coins/list").subList(0, 5).stream().map(CoinDto::getId).collect(toList());
    }
}
