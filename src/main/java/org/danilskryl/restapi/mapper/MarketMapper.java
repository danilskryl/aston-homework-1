package org.danilskryl.restapi.mapper;

import org.danilskryl.restapi.dto.MarketTo;
import org.danilskryl.restapi.model.Market;

public class MarketMapper implements Mapper<Market, MarketTo> {

    @Override
    public Market toEntity(MarketTo dto) {
        Market market = new Market();
        market.setId(dto.getId());
        market.setName(dto.getName());

        return market;
    }

    @Override
    public MarketTo toDto(Market market) {
        return MarketTo.builder()
                .id(market.getId())
                .name(market.getName())
                .build();
    }
}
