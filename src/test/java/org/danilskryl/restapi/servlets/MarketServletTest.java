package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.MarketTo;
import org.danilskryl.restapi.servlets.data.market.MarketDataTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.danilskryl.restapi.servlets.data.util.DatabaseScriptRunner.executeScript;
import static org.danilskryl.restapi.servlets.data.util.TestUtil.removeFieldFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MarketServletTest {
    @InjectMocks
    MarketServlet marketServlet;
    ObjectMapper mapper = new ObjectMapper();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter stringWriter = new StringWriter();

    @BeforeAll
    static void runSqlScripts() {
        executeScript("src/main/resources/ddl.sql");
        executeScript("src/main/resources/dml.sql");
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    @Order(1)
    void testDoGetMarketWithValidId(Long id) throws Exception {
        when(request.getPathInfo()).thenReturn("/" + id);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        marketServlet.doGet(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString();

        assertEquals(MarketDataTest.ARRAY_MARKET_JSON[Math.toIntExact(id - 1)], actualJson);
    }

    @Test
    @Order(2)
    void testDoGetAllMarkets() throws IOException {
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        marketServlet.doGet(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString();

        assertEquals(MarketDataTest.ALL_MARKETS_JSON, actualJson);
    }

    @Test
    @Order(3)
    void testDoPostValidMarket() throws IOException {
        MarketTo newMarketTo = new MarketTo();
        newMarketTo.setName("TestMarket");

        String newMarketJson = mapper.writeValueAsString(newMarketTo);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(newMarketJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        marketServlet.doPost(request, response);

        verify(response, times(1)).setStatus(SC_CREATED);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();

        assertEquals(MarketDataTest.NEW_MARKET_JSON, actualJson);
    }

    @Test
    @Order(4)
    void testDoPutValidMarket() throws IOException {
        MarketTo updatedMarketTo = new MarketTo();
        updatedMarketTo.setId(4L);
        updatedMarketTo.setName("UpdatedTestMarket");

        String updatedMarketJson = mapper.writeValueAsString(updatedMarketTo);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(updatedMarketJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        marketServlet.doPut(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();

        assertEquals(MarketDataTest.UPDATED_MARKET_JSON, actualJson);
    }

    @Test
    @Order(5)
    void testDoDeleteValidMarket() throws Exception {
        when(request.getPathInfo()).thenReturn("/4");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        marketServlet.doDelete(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();
        actualJson = removeFieldFromJson(actualJson, "issueAt");

        assertEquals(MarketDataTest.SUCCESSFUL_DELETE_MARKET_JSON, actualJson);
    }
}
