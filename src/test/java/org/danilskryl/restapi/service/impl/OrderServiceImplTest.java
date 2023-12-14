package org.danilskryl.restapi.service.impl;

import org.danilskryl.restapi.dto.OrderDto;
import org.danilskryl.restapi.mapper.Mapper;
import org.danilskryl.restapi.model.Order;
import org.danilskryl.restapi.repository.impl.OrderRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepositoryImpl orderRepository;
    @Mock
    private Mapper<Order, OrderDto> mapper;
    private OrderServiceImpl orderService;
    private OrderDto expectedOrderDto;
    private Order expectedOrder;

    @BeforeEach
    void setup() {
        orderService = new OrderServiceImpl(orderRepository, mapper);

        expectedOrderDto = OrderDto.builder()
                .id(1L)
                .orderDate(LocalDateTime.parse("2022-08-12T12:33:22"))
                .build();

        expectedOrder = new Order();
        expectedOrder.setId(1L);
        expectedOrder.setOrderDate(LocalDateTime.parse("2022-08-12T12:33:22"));
    }

    @Test
    void getMarketTest() {
        when(orderRepository.getById(anyLong())).thenReturn(expectedOrder);
        when(orderService.getById(anyLong())).thenReturn(expectedOrderDto);

        OrderDto actualOrderDto = orderService.getById(1L);

        verify(mapper).toDto(expectedOrder);
        assertEquals(expectedOrderDto, actualOrderDto);
    }

    @Test
    void saveMarketTest() {
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);
        when(mapper.fromDto(any(OrderDto.class))).thenReturn(expectedOrder);

        orderService.save(expectedOrderDto);

        verify(mapper).fromDto(expectedOrderDto);
        verify(orderRepository).save(expectedOrder);
        verify(mapper).toDto(expectedOrder);
    }

    @Test
    void updateMarketTest() {
        when(mapper.fromDto(any(OrderDto.class))).thenReturn(expectedOrder);
        when(orderRepository.update(any(Order.class))).thenReturn(expectedOrder);
        when(mapper.toDto(any(Order.class))).thenReturn(expectedOrderDto);

        OrderDto actualOrderDto = orderService.update(expectedOrderDto);

        verify(mapper).fromDto(expectedOrderDto);
        verify(orderRepository).update(expectedOrder);
        verify(mapper).toDto(expectedOrder);
        assertEquals(expectedOrderDto, actualOrderDto);
    }
}