package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.MarketDto;
import org.danilskryl.restapi.exception.ResponseData;
import org.danilskryl.restapi.service.MarketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.danilskryl.restapi.servlets.util.TestUtil.removeFieldFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarketServletTest {
    @InjectMocks
    MarketServlet marketServlet;
    @Mock
    MarketService marketService;
    @Spy
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    StringWriter stringWriter = new StringWriter();
    List<MarketDto> expectedMarketDtoList = List.of(
            new MarketDto(23L, "Test23"),
            new MarketDto(113L, "Test113"),
            new MarketDto(18841L, "18841Test")
    );

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void testDoGetMarketWithValidId(Long id) throws Exception {
        MarketDto expectedMarketDto = new MarketDto(id, "Test market");

        when(request.getPathInfo()).thenReturn("/" + id);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(marketService.getById(id)).thenReturn(expectedMarketDto);

        marketServlet.doGet(request, response);
        String actualJson = stringWriter.toString();
        MarketDto actualMarketDto = mapper.readValue(actualJson, MarketDto.class);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(marketService, times(1)).getById(id);
        assertEquals(id, expectedMarketDto.getId());
        assertEquals(expectedMarketDto, actualMarketDto);
    }

    @Test
    void testDoGetAllMarkets() throws IOException {
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(marketService.getAll()).thenReturn(expectedMarketDtoList);

        marketServlet.doGet(request, response);

        String actualJson = stringWriter.toString();
        List<MarketDto> actualMarketDtoList = mapper.readValue(actualJson, new TypeReference<>() {
        });

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(marketService, times(1)).getAll();
        assertEquals(expectedMarketDtoList, actualMarketDtoList);

    }

    @Test
    void testDoPostValidMarket() throws IOException {
        MarketDto newMarketDto = new MarketDto();
        newMarketDto.setId(23L);
        newMarketDto.setName("TestMarket");

        String newMarketJson = mapper.writeValueAsString(newMarketDto);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(newMarketJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(marketService.save(newMarketDto)).thenReturn(newMarketDto);

        marketServlet.doPost(request, response);

        String actualJson = stringWriter.toString().trim();
        MarketDto actualMarketDto = mapper.readValue(actualJson, MarketDto.class);

        verify(response, times(1)).setStatus(SC_CREATED);
        verify(response, times(1)).setContentType("application/json");
        verify(marketService, times(1)).save(newMarketDto);
        assertEquals(newMarketDto, actualMarketDto);
    }

    @Test
    void testDoPutValidMarket() throws IOException {
        MarketDto updatedMarketDto = new MarketDto();
        updatedMarketDto.setId(4L);
        updatedMarketDto.setName("UpdatedTestMarket");

        String updatedMarketJson = mapper.writeValueAsString(updatedMarketDto);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(updatedMarketJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(marketService.update(updatedMarketDto)).thenReturn(updatedMarketDto);

        marketServlet.doPut(request, response);

        String actualJson = stringWriter.toString().trim();
        MarketDto actualMarketDto = mapper.readValue(actualJson, MarketDto.class);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(marketService, times(1)).update(updatedMarketDto);
        assertEquals(updatedMarketDto, actualMarketDto);
    }

    @Test
    void testDoDeleteValidMarket() throws Exception {
        ResponseData expectedResponseData = ResponseData.builder()
                .status(200)
                .message("Successfully removed=true")
                .build();

        when(request.getPathInfo()).thenReturn("/4");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(marketService.remove(4L)).thenReturn(true);

        marketServlet.doDelete(request, response);

        String actualJson = stringWriter.toString().trim();
        actualJson = removeFieldFromJson(actualJson, "issueAt");
        ResponseData actualResponseData = mapper.readValue(actualJson, ResponseData.class);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(marketService, times(1)).remove(anyLong());
        assertEquals(expectedResponseData, actualResponseData);
    }
}
