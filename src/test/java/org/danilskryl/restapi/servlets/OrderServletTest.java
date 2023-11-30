package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.OrderProductTo;
import org.danilskryl.restapi.dto.OrderTo;
import org.danilskryl.restapi.servlets.data.order.OrderDataTest;
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
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.danilskryl.restapi.servlets.data.util.DatabaseScriptRunner.executeScript;
import static org.danilskryl.restapi.servlets.data.util.TestUtil.removeFieldFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServletTest {
    @InjectMocks
    OrderServlet orderServlet;
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter stringWriter = new StringWriter();
    static LocalDateTime TEMPLATE_ORDER_DATE;

    @BeforeAll
    static void runSqlScripts() {
        executeScript("src/main/resources/ddl.sql");
        executeScript("src/main/resources/dml.sql");
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @Order(1)
    void testDoGetOrderWithValidId(Long id) throws Exception {
        when(request.getPathInfo()).thenReturn("/" + id);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        orderServlet.doGet(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString();

        assertEquals(OrderDataTest.ARRAY_ORDER_JSON[Math.toIntExact(id - 1)], actualJson);
    }

    @Test
    @Order(2)
    void testDoGetAllOrders() throws IOException {
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        orderServlet.doGet(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();

        assertEquals(OrderDataTest.ALL_ORDERS_JSON, actualJson);
    }

    @Test
    @Order(3)
    void testDoPostValidOrder() throws IOException {
        OrderTo newOrderTo = new OrderTo();
        newOrderTo.setOrderDate(LocalDateTime.now());

        OrderProductTo orderProductTo = new OrderProductTo();
        orderProductTo.setOrderTo(newOrderTo);
        orderProductTo.setProductsId(List.of(8L));

        String newOrderProductJson = mapper.writeValueAsString(orderProductTo);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(newOrderProductJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        orderServlet.doPost(request, response);

        verify(response, times(1)).setStatus(SC_CREATED);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();
        OrderTo actualOrderTo = mapper.readValue(actualJson, OrderTo.class);

        assertEquals(11L, actualOrderTo.getId());
        assertNotNull(actualOrderTo.getOrderDate());

        TEMPLATE_ORDER_DATE = newOrderTo.getOrderDate();
    }

    @Test
    @Order(4)
    void testDoPutValidProduct() throws IOException {
        OrderTo updatedOrderTo = new OrderTo();
        updatedOrderTo.setId(11L);
        updatedOrderTo.setOrderDate(LocalDateTime.parse(OrderDataTest.ORDER_DATE));

        String newOrderJson = mapper.writeValueAsString(updatedOrderTo);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(newOrderJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        orderServlet.doPut(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();

        assertEquals(OrderDataTest.UPDATED_ORDER_JSON, actualJson);
    }

    @Test
    @Order(5)
    void testDoDeleteValidProduct() throws Exception {
        when(request.getPathInfo()).thenReturn("/11");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        orderServlet.doDelete(request, response);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");

        String actualJson = stringWriter.toString().trim();
        actualJson = removeFieldFromJson(actualJson, "issueAt");

        assertEquals(OrderDataTest.SUCCESSFUL_DELETE_ORDER_JSON, actualJson);
    }
}