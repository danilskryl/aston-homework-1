package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.ProductDto;
import org.danilskryl.restapi.service.ProductService;
import org.danilskryl.restapi.util.ResponseData;
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
class ProductServletTest {
    @InjectMocks
    ProductServlet productServlet;
    @Mock
    ProductService productService;
    @Spy
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    StringWriter stringWriter = new StringWriter();
    List<ProductDto> expectedProductDtoList = List.of(
            new ProductDto(23L, "Test23", "Test description 23", 23L),
            new ProductDto(113L, "Test113", "Test description22222", 11L),
            new ProductDto(18841L, "18841Test", "Test description", 100L)
    );

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8})
    void testDoGetProductWithValidId(Long id) throws Exception {
        ProductDto expectedProductDto = new ProductDto(id, "Test market", "Test description", 23L);

        when(request.getPathInfo()).thenReturn("/" + id);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(productService.getById(id)).thenReturn(expectedProductDto);

        productServlet.doGet(request, response);

        String actualJson = stringWriter.toString();
        ProductDto actualProductDto = mapper.readValue(actualJson, ProductDto.class);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(productService, times(1)).getById(id);
        assertEquals(id, expectedProductDto.getId());
        assertEquals(expectedProductDto, actualProductDto);
    }

    @Test
    void testDoGetAllProducts() throws IOException {
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(productService.getAll()).thenReturn(expectedProductDtoList);

        productServlet.doGet(request, response);

        String actualJson = stringWriter.toString();
        List<ProductDto> actualMarketDtoList = mapper.readValue(actualJson, new TypeReference<>() {
        });

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(productService, times(1)).getAll();
        assertEquals(expectedProductDtoList, actualMarketDtoList);

    }

    @Test
    void testDoPostValidProduct() throws IOException {
        ProductDto newProductDto = expectedProductDtoList.get(0);

        String newMarketJson = mapper.writeValueAsString(newProductDto);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(newMarketJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(productService.save(newProductDto)).thenReturn(newProductDto);

        productServlet.doPost(request, response);

        String actualJson = stringWriter.toString().trim();
        ProductDto actualProductDto = mapper.readValue(actualJson, ProductDto.class);

        verify(response, times(1)).setStatus(SC_CREATED);
        verify(response, times(1)).setContentType("application/json");
        verify(productService, times(1)).save(newProductDto);
        assertEquals(newProductDto, actualProductDto);
    }

    @Test
    void testDoPutValidProduct() throws IOException {
        ProductDto updatedProductDto = expectedProductDtoList.get(1);

        String updatedMarketJson = mapper.writeValueAsString(updatedProductDto);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(updatedMarketJson)));
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(productService.update(updatedProductDto)).thenReturn(updatedProductDto);

        productServlet.doPut(request, response);

        String actualJson = stringWriter.toString().trim();
        ProductDto actualProductDto = mapper.readValue(actualJson, ProductDto.class);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(productService, times(1)).update(updatedProductDto);
        assertEquals(updatedProductDto, actualProductDto);
    }

    @Test
    void testDoDeleteValidProduct() throws Exception {
        ResponseData expectedResponseData = ResponseData.builder()
                .status(200)
                .message("Successfully removed=true")
                .build();

        when(request.getPathInfo()).thenReturn("/4");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(productService.remove(4L)).thenReturn(true);

        productServlet.doDelete(request, response);

        String actualJson = stringWriter.toString().trim();
        actualJson = removeFieldFromJson(actualJson, "issueAt");
        ResponseData actualResponseData = mapper.readValue(actualJson, ResponseData.class);

        verify(response, times(1)).setStatus(SC_OK);
        verify(response, times(1)).setContentType("application/json");
        verify(productService, times(1)).remove(anyLong());
        assertEquals(expectedResponseData, actualResponseData);
    }
}