package com.km.cryptocurrencies;

import com.km.cryptocurrencies.dto.CryptoCurrencyDto;
import com.km.cryptocurrencies.dto.VsCurrenciesWithData;
import com.km.cryptocurrencies.service.IntegrationService;
import com.km.cryptocurrencies.repository.CurrencyRepository;
import com.km.cryptocurrencies.repository.CurrencyValueRepository;
import com.km.cryptocurrencies.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    IntegrationService integrationService;
    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private CurrencyValueRepository currencyValueRepository;

    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(integrationService, currencyRepository, currencyValueRepository);
    }

    @Test
    void shouldReturnHistoryValuesForCurrency() {
        //given
        String coinId = "id2";

        given(currencyRepository.findAllHistory("id2")).willReturn(createResponseList().subList(1,2));
        given(currencyValueRepository.findVsCurrenciesWithData(2L)).willReturn(createResponseVsCurrenciesWithDataList2());

        //when
        List<CryptoCurrencyDto> result = currencyService.getHistoryValuesForCoinId(coinId);

        //then
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getValues().size(), 2);
        assertTrue(result.get(0).getValues().containsKey("btn"));
        assertTrue(result.get(0).getValues().containsKey("eth"));
    }

    @Test
    void shouldReturnAllHistoryValues() {
        //given
        given(currencyRepository.findAllHistory()).willReturn(createResponseList());
        given(currencyValueRepository.findVsCurrenciesWithData(1L)).willReturn(createResponseVsCurrenciesWithDataList());
        given(currencyValueRepository.findVsCurrenciesWithData(2L)).willReturn(createResponseVsCurrenciesWithDataList());
        given(currencyValueRepository.findVsCurrenciesWithData(3L)).willReturn(createResponseVsCurrenciesWithDataList());

        //when
        List<CryptoCurrencyDto> result = currencyService.getAllHistoryValues();

        //then
        assertEquals(result.size(), 3);
        assertEquals(result.get(0).getValues().size(), 3);
        assertTrue(result.get(0).getValues().containsKey("btn"));
        assertTrue(result.get(0).getValues().containsKey("eth"));
        assertEquals(result.get(0).getValues().get("eth").size(), 1);
        assertEquals(result.get(0).getValues().get("pln").size(), 2);
    }

    private static List<CryptoCurrencyDto> createResponseList() {
        List<CryptoCurrencyDto> list = new ArrayList<>();
        list.add(new CryptoCurrencyDto(1L, "id1"));
        list.add(new CryptoCurrencyDto(2L, "id2"));
        list.add(new CryptoCurrencyDto(3L, "id3"));
        return list;
    }

    private static List<VsCurrenciesWithData> createResponseVsCurrenciesWithDataList() {
        List<VsCurrenciesWithData> list = new ArrayList<>();
        list.add(createVsCurrencyWithData("btn", LocalDateTime.now().minusSeconds(30)));
        list.add(createVsCurrencyWithData("eth", LocalDateTime.now()));
        list.add(createVsCurrencyWithData("btn", LocalDateTime.now().plusSeconds(30)));
        list.add(createVsCurrencyWithData("pln", LocalDateTime.now()));
        list.add(createVsCurrencyWithData("pln", LocalDateTime.now()));
        list.add(createVsCurrencyWithData("btn", LocalDateTime.now()));
        return list;
    }

    private static List<VsCurrenciesWithData> createResponseVsCurrenciesWithDataList2() {
        List<VsCurrenciesWithData> list = new ArrayList<>();
        list.add(createVsCurrencyWithData("btn", LocalDateTime.now().minusSeconds(30)));
        list.add(createVsCurrencyWithData("eth", LocalDateTime.now()));
        list.add(createVsCurrencyWithData("btn", LocalDateTime.now()));
        return list;
    }

    private static VsCurrenciesWithData createVsCurrencyWithData(String eth, LocalDateTime time) {
        return VsCurrenciesWithData.builder().vsCurrency(eth).value(Math.random()).creationTime(time).build();
    }

}
