package org.danilskryl.restapi.mapper;

import org.danilskryl.restapi.dto.MarketDto;
import org.danilskryl.restapi.model.Market;

public class MarketMapper implements Mapper<Market, MarketDto> {

    @Override
    public Market fromDto(MarketDto dto) {
        Market market = new Market();
        market.setId(dto.getId());
        market.setName(dto.getName());

        return market;
    }

    @Override
    public MarketDto toDto(Market market) {
        return MarketDto.builder()
                .id(market.getId())
                .name(market.getName())
                .build();
    }
}
