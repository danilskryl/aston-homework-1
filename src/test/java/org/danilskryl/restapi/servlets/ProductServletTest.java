package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.ProductTo;
import org.danilskryl.restapi.servlets.data.product.ProductDataTest;
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
class ProductServletTest {
    @InjectMocks
    ProductServlet productServlet;
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
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @Order(1)
    void testDoGetProductWithValidId(Long id) throws Exception {
        when(request.getPathInfo()).thenReturn("/" + id);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        productServlet.doGet(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString();

        assertEquals(ProductDataTest.ARRAY_PRODUCT_JSON[Math.toIntExact(id - 1)], actualJson);
    }

    @Test
    @Order(2)
    void testDoGetAllProducts() throws IOException {
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        productServlet.doGet(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();

        assertEquals(ProductDataTest.ALL_PRODUCTS_JSON, actualJson);
    }

    @Test
    @Order(3)
    void testDoPostValidProduct() throws IOException {
        ProductTo newProductTo = new ProductTo();
        newProductTo.setName("TestProduct");
        newProductTo.setDescription("TestDescription");
        newProductTo.setMarketId(1L);

        String newProductJson = mapper.writeValueAsString(newProductTo);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(newProductJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        productServlet.doPost(request, response);

        verify(response, times(1)).setStatus(SC_CREATED);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();

        assertEquals(ProductDataTest.NEW_PRODUCT_JSON, actualJson);
    }

    @Test
    @Order(4)
    void testDoPutValidProduct() throws IOException {
        ProductTo newProductTo = new ProductTo();
        newProductTo.setId(11L);
        newProductTo.setName("UpdatedTestProduct");
        newProductTo.setDescription("UpdatedTestDescription");
        newProductTo.setMarketId(2L);

        String newProductJson = mapper.writeValueAsString(newProductTo);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(newProductJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        productServlet.doPut(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();

        assertEquals(ProductDataTest.UPDATED_PRODUCT_JSON, actualJson);
    }

    @Test
    @Order(5)
    void testDoDeleteValidProduct() throws Exception {
        when(request.getPathInfo()).thenReturn("/11");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        productServlet.doDelete(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();
        actualJson = removeFieldFromJson(actualJson, "issueAt");

        assertEquals(ProductDataTest.SUCCESSFUL_DELETE_PRODUCT_JSON, actualJson);
    }
}