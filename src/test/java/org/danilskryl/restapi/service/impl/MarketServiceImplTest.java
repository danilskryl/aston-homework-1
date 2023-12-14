package org.danilskryl.restapi.service.impl;

import org.danilskryl.restapi.dto.MarketDto;
import org.danilskryl.restapi.mapper.Mapper;
import org.danilskryl.restapi.model.Market;
import org.danilskryl.restapi.repository.impl.MarketRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarketServiceImplTest {
    @Mock
    private MarketRepositoryImpl marketRepository;
    @Mock
    private Mapper<Market, MarketDto> mapper;
    private MarketServiceImpl marketService;
    private MarketDto expectedMarketDto;
    private Market expectedMarket;

    @BeforeEach
    void setup() {
        marketService = new MarketServiceImpl(marketRepository, mapper);

        expectedMarketDto = MarketDto.builder()
                .id(1L)
                .name("Test market")
                .build();

        expectedMarket = new Market();
        expectedMarket.setId(1L);
        expectedMarket.setName("Test market");
    }

    @Test
    void getMarketTest() {
        when(marketRepository.getById(anyLong())).thenReturn(expectedMarket);
        when(marketService.getById(anyLong())).thenReturn(expectedMarketDto);

        MarketDto actualMarketDto = marketService.getById(1L);

        verify(mapper).toDto(expectedMarket);
        assertEquals(expectedMarketDto, actualMarketDto);
    }

    @Test
    void saveMarketTest() {
        when(marketRepository.save(any(Market.class))).thenReturn(expectedMarket);
        when(mapper.fromDto(any(MarketDto.class))).thenReturn(expectedMarket);

        marketService.save(expectedMarketDto);

        verify(mapper).fromDto(expectedMarketDto);
        verify(marketRepository).save(expectedMarket);
        verify(mapper).toDto(expectedMarket);
    }

    @Test
    void updateMarketTest() {
        when(mapper.fromDto(any(MarketDto.class))).thenReturn(expectedMarket);
        when(marketRepository.update(any(Market.class))).thenReturn(expectedMarket);
        when(mapper.toDto(any(Market.class))).thenReturn(expectedMarketDto);

        MarketDto actualMarketDto = marketService.update(expectedMarketDto);

        verify(mapper).fromDto(expectedMarketDto);
        verify(marketRepository).update(expectedMarket);
        verify(mapper).toDto(expectedMarket);
        assertEquals(expectedMarketDto, actualMarketDto);
    }
}