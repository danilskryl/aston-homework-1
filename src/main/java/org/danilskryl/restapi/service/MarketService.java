package org.danilskryl.restapi.service;

import org.danilskryl.restapi.dto.MarketDto;

import java.util.List;

public interface MarketService extends BaseService<MarketDto> {
    List<MarketDto> getAll();

    MarketDto getById(Long id);

    MarketDto save(MarketDto market);

    MarketDto update(MarketDto marketDto);

    boolean remove(Long id);
}
