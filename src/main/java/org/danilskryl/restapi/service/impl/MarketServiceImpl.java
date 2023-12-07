package org.danilskryl.restapi.service.impl;


import org.danilskryl.restapi.dto.MarketDto;
import org.danilskryl.restapi.mapper.Mapper;
import org.danilskryl.restapi.mapper.MarketMapper;
import org.danilskryl.restapi.model.Market;
import org.danilskryl.restapi.repository.MarketRepository;
import org.danilskryl.restapi.repository.impl.MarketRepositoryImpl;
import org.danilskryl.restapi.service.MarketService;

import java.util.List;

public class MarketServiceImpl implements MarketService {
    private final MarketRepository marketRepository;
    private final Mapper<Market, MarketDto> mapper;

    public MarketServiceImpl() {
        this.marketRepository = new MarketRepositoryImpl();
        mapper = new MarketMapper();
    }

    public MarketServiceImpl(MarketRepository marketRepository, Mapper<Market, MarketDto> mapper) {
        this.marketRepository = marketRepository;
        this.mapper = mapper;
    }

    @Override
    public List<MarketDto> getAll() {
        List<Market> marketList = marketRepository.getAll();
        return marketList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public MarketDto getById(Long id) {
        Market market = marketRepository.getById(id);
        return mapper.toDto(market);
    }

    @Override
    public MarketDto save(MarketDto marketDto) {
        Market market = mapper.fromDto(marketDto);
        Market savedMarket = marketRepository.save(market);
        return mapper.toDto(savedMarket);
    }

    @Override
    public MarketDto update(MarketDto marketDto) {
        Market market = mapper.fromDto(marketDto);
        Market updatedMarket = marketRepository.update(market);
        return mapper.toDto(updatedMarket);
    }

    @Override
    public boolean remove(Long id) {
        return marketRepository.remove(id);
    }
}