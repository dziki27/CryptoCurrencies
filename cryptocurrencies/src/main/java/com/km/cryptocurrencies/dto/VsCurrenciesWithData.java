package com.km.cryptocurrencies.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VsCurrenciesWithData {

    String vsCurrency;
    Double value;
    LocalDateTime creationTime;
}
