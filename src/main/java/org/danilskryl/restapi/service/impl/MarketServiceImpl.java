package org.danilskryl.restapi.service.impl;


import org.danilskryl.restapi.dao.MarketDAO;
import org.danilskryl.restapi.dto.MarketTo;
import org.danilskryl.restapi.mapper.Mapper;
import org.danilskryl.restapi.mapper.MarketMapper;
import org.danilskryl.restapi.model.Market;
import org.danilskryl.restapi.service.MarketService;

import java.util.List;

public class MarketServiceImpl implements MarketService {
    private final MarketDAO marketDAO;
    private final Mapper<Market, MarketTo> mapper = new MarketMapper();

    public MarketServiceImpl() {
        this.marketDAO = new MarketDAO();
    }

    @Override
    public List<MarketTo> getAllMarkets() {
        return marketDAO.getAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public MarketTo getMarketById(Long id) {
        return mapper.toDto(marketDAO.getById(id));
    }

    @Override
    public MarketTo saveMarket(MarketTo marketTo) {
        Market market = marketDAO.save(mapper.toEntity(marketTo));
        return mapper.toDto(market);
    }

    @Override
    public MarketTo updateMarket(MarketTo marketTo) {
        Market market = marketDAO.update(mapper.toEntity(marketTo));
        return mapper.toDto(market);
    }

    @Override
    public boolean removeMarket(Long id) {
        return marketDAO.remove(id);
    }
}