package org.danilskryl.restapi.service;

import org.danilskryl.restapi.dto.MarketTo;

import java.util.List;

public interface MarketService {
    List<MarketTo> getAllMarkets();

    MarketTo getMarketById(Long id);

    MarketTo saveMarket(MarketTo market);

    MarketTo updateMarket(MarketTo marketTo);

    boolean removeMarket(Long id);
}
