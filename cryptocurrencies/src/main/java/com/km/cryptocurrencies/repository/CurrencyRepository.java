package com.km.cryptocurrencies.repository;

import com.km.cryptocurrencies.dto.CryptoCurrencyDto;
import com.km.cryptocurrencies.entity.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CryptoCurrency, Integer> {

    @Query("SELECT c FROM CryptoCurrency c WHERE c.coinId =:coinId")
    Optional<CryptoCurrency> findByCoinId(String coinId);

    @Query("SELECT new com.km.cryptocurrencies.dto.CryptoCurrencyDto(cc.id, cc.coinId) FROM CryptoCurrency cc WHERE cc.coinId =:coinId")
    List<CryptoCurrencyDto> findAllHistory(String coinId);

    @Query("SELECT new com.km.cryptocurrencies.dto.CryptoCurrencyDto(cc.id, cc.coinId) FROM CryptoCurrency cc")
    List<CryptoCurrencyDto> findAllHistory();

}
