package com.km.cryptocurrencies.repository;

import com.km.cryptocurrencies.dto.VsCurrenciesWithData;
import com.km.cryptocurrencies.entity.CurrencyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyValueRepository extends JpaRepository<CurrencyValue, Integer> {

    @Query("SELECT new com.km.cryptocurrencies.dto.VsCurrenciesWithData(cv.currency, cv.value, cv.creationDate) FROM CurrencyValue cv WHERE cv.cryptoCurrency.id =:id")
    List<VsCurrenciesWithData> findVsCurrenciesWithData(Long id);

}
