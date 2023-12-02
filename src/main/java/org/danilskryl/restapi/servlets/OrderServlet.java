package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.OrderProductTo;
import org.danilskryl.restapi.dto.OrderTo;
import org.danilskryl.restapi.exception.ResponseData;
import org.danilskryl.restapi.service.OrderService;
import org.danilskryl.restapi.service.impl.OrderServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(name = "OrderServlet", value = "/api/v1/orders/*")
public class OrderServlet extends HttpServlet {
    private final OrderService service = new OrderServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");

        if (pathInfo == null || pathInfo.equals("/")) {
            List<OrderTo> allOrders = service.getAllOrders();
            String json = mapper.writeValueAsString(allOrders);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);

            try {
                long orderId = Long.parseLong(idString);
                OrderTo orderTo = service.getOrderById(orderId);

                if (orderTo == null) {
                    resp.setStatus(SC_NOT_FOUND);
                    resp.getWriter().write(mapper.writeValueAsString(
                            ResponseData.constructResponseData(SC_NOT_FOUND, "Order not found with ID " + orderId)
                    ));
                    return;
                }

                String json = mapper.writeValueAsString(orderTo);
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
        resp.setContentType("application/json");

        if (isPathNotNull(req.getPathInfo())) {
            resp.getWriter().write(mapper.writeValueAsString(
                    ResponseData.constructResponseData(
                            SC_BAD_REQUEST, "Invalid path"
                    )
            ));
            resp.setStatus(SC_BAD_REQUEST);
            return;
        }
        OrderProductTo orderProductTo = mapper.readValue(readJson(req.getReader()), OrderProductTo.class);

        OrderTo orderTo = service.saveOrder(orderProductTo.getOrderTo(), orderProductTo.getProductsId());

        String json = mapper.writeValueAsString(orderTo);
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
        OrderTo convertedOrder = mapper.readValue(readJson(req.getReader()), OrderTo.class);

        OrderTo orderTo = service.updateOrder(convertedOrder);

        String json = mapper.writeValueAsString(orderTo);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        if (isPathNotNull(pathInfo) && !pathInfo.equals("/")) {
            String idString = pathInfo.substring(1);

            try {
                long orderId = Long.parseLong(idString);

                boolean b = service.removeOrder(orderId);

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