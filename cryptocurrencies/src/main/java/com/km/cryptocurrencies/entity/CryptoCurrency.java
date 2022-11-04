package com.km.cryptocurrencies.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CryptoCurrency {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    Long id;

    String coinId;

    @CreationTimestamp
    LocalDateTime creationDate;

    @UpdateTimestamp
    LocalDateTime modificationDate;

    @OneToMany(fetch = FetchType.LAZY)
    List<CurrencyValue> currencyValues;
}
