package com.km.cryptocurrencies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrencyDto {

    private Long id;
    private String coinId;

    private Map<String, List<ValueWithDateDto>> values;

    public CryptoCurrencyDto(Long id, String coinId) {
        this.id = id;
        this.coinId = coinId;
        this.values = new HashMap<>();
    }
}
