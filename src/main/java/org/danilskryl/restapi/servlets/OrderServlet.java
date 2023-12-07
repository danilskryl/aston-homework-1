package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.OrderDto;
import org.danilskryl.restapi.dto.OrderProductDto;
import org.danilskryl.restapi.exception.ResponseData;
import org.danilskryl.restapi.service.OrderService;
import org.danilskryl.restapi.service.impl.OrderServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "OrderServlet", value = "/api/v1/orders/*")
public class OrderServlet extends HttpServlet {
    private final OrderService service;
    private final ObjectMapper mapper;
    private static final String CONTENT_TYPE = "application/json";

    public OrderServlet() {
        service = new OrderServiceImpl();
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public OrderServlet(OrderService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE);

        if (pathInfo == null || pathInfo.equals("/")) {
            List<OrderDto> allOrders = service.getAll();
            String json = mapper.writeValueAsString(allOrders);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);

            try {
                long orderId = Long.parseLong(idString);
                OrderDto orderDto = service.getById(orderId);

                if (orderDto == null) {
                    resp.setStatus(SC_NOT_FOUND);
                    resp.getWriter().write(mapper.writeValueAsString(
                            ResponseData.constructResponseData(SC_NOT_FOUND, "Order not found with ID " + orderId)
                    ));
                    return;
                }

                String json = mapper.writeValueAsString(orderDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(json);
            } catch (NumberFormatException e) {
                String json = mapper.writeValueAsString(ResponseData.constructResponseData(
                        SC_BAD_REQUEST, "ID should be a number."
                ));
                resp.setStatus(SC_BAD_REQUEST);
                resp.getWriter().write(json);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(CONTENT_TYPE);

        if (isPathNotNull(req.getPathInfo())) {
            resp.getWriter().write(mapper.writeValueAsString(
                    ResponseData.constructResponseData(
                            SC_BAD_REQUEST, "Invalid path"
                    )
            ));
            resp.setStatus(SC_BAD_REQUEST);
            return;
        }
        OrderProductDto orderProductDto = mapper.readValue(readJson(req.getReader()), OrderProductDto.class);

        OrderDto orderDto = service.save(orderProductDto.getOrderDto(), orderProductDto.getProductsId());

        String json = mapper.writeValueAsString(orderDto);
        resp.setStatus(SC_CREATED);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isPathNotNull(req.getPathInfo())) {
            resp.getWriter().write(mapper.writeValueAsString(
                    ResponseData.constructResponseData(SC_BAD_REQUEST, "Invalid path")
            ));
            resp.setStatus(SC_BAD_REQUEST);
            return;
        }
        OrderDto convertedOrder = mapper.readValue(readJson(req.getReader()), OrderDto.class);

        OrderDto orderDto = service.update(convertedOrder);

        String json = mapper.writeValueAsString(orderDto);
        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(CONTENT_TYPE);

        String pathInfo = req.getPathInfo();
        if (isPathNotNull(pathInfo) && !pathInfo.equals("/")) {
            String idString = pathInfo.substring(1);

            try {
                long orderId = Long.parseLong(idString);

                boolean b = service.remove(orderId);

                resp.setStatus(SC_OK);
                resp.getWriter().write(mapper.writeValueAsString(
                        ResponseData.constructResponseData(SC_OK, "Successfully removed=" + b))
                );
            } catch (NumberFormatException e) {
                resp.setStatus(SC_BAD_REQUEST);
                resp.getWriter().write(mapper.writeValueAsString(
                        ResponseData.constructResponseData(SC_BAD_REQUEST, "Path must be contains only numbers")
                ));
            }
        }
    }

    private String readJson(BufferedReader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    private boolean isPathNotNull(String path) {
        return path != null;
    }
}