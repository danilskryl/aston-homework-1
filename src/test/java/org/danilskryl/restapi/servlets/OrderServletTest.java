package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.OrderDto;
import org.danilskryl.restapi.exception.ResponseData;
import org.danilskryl.restapi.service.OrderService;
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
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.danilskryl.restapi.servlets.util.TestUtil.removeFieldFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServletTest {
    @InjectMocks
    OrderServlet orderServlet;
    @Mock
    OrderService orderService;
    @Spy
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    StringWriter stringWriter = new StringWriter();
    List<OrderDto> expectedOrderDtoList = List.of(
            new OrderDto(23L, LocalDateTime.parse("2022-09-02T13:21:33")),
            new OrderDto(142L, LocalDateTime.parse("2023-01-01T16:30:00")),
            new OrderDto(18841L, LocalDateTime.parse("2022-09-02T11:44:13"))
    );

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8})
    void testDoGetOrderWithValidId(Long id) throws Exception {
        OrderDto expectedOrderDto = new OrderDto(id, LocalDateTime.parse("2001-08-12T16:00:00"));

        when(request.getPathInfo()).thenReturn("/" + id);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(orderService.getById(id)).thenReturn(expectedOrderDto);

        orderServlet.doGet(request, response);

        String actualJson = stringWriter.toString();
        OrderDto actualProductDto = mapper.readValue(actualJson, OrderDto.class);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(orderService, times(1)).getById(id);
        assertEquals(id, expectedOrderDto.getId());
        assertEquals(expectedOrderDto, actualProductDto);
    }

    @Test
    void testDoGetAllOrders() throws IOException {
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(orderService.getAll()).thenReturn(expectedOrderDtoList);

        orderServlet.doGet(request, response);

        String actualJson = stringWriter.toString();
        List<OrderDto> actualOrderDtoList = mapper.readValue(actualJson, new TypeReference<>() {
        });

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(orderService, times(1)).getAll();
        assertEquals(expectedOrderDtoList, actualOrderDtoList);

    }

    @Test
    void testDoPutValidOrder() throws IOException {
        OrderDto updatedOrderDto = expectedOrderDtoList.get(1);

        String updatedOrderJson = mapper.writeValueAsString(updatedOrderDto);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(updatedOrderJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(orderService.update(updatedOrderDto)).thenReturn(updatedOrderDto);

        orderServlet.doPut(request, response);

        String actualJson = stringWriter.toString().trim();
        OrderDto actualOrderDto = mapper.readValue(actualJson, OrderDto.class);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(orderService, times(1)).update(updatedOrderDto);
        assertEquals(updatedOrderDto, actualOrderDto);
    }

    @Test
    void testDoDeleteValidOrder() throws Exception {
        ResponseData expectedResponseData = ResponseData.builder()
                .status(200)
                .message("Successfully removed=true")
                .build();

        when(request.getPathInfo()).thenReturn("/4");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(orderService.remove(4L)).thenReturn(true);

        orderServlet.doDelete(request, response);

        String actualJson = stringWriter.toString().trim();
        actualJson = removeFieldFromJson(actualJson, "issueAt");
        ResponseData actualResponseData = mapper.readValue(actualJson, ResponseData.class);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(orderService, times(1)).remove(anyLong());
        assertEquals(expectedResponseData, actualResponseData);
    }
}