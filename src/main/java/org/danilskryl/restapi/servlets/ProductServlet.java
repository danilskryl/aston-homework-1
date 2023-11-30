package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.ProductTo;
import org.danilskryl.restapi.service.ProductService;
import org.danilskryl.restapi.service.impl.ProductServiceImpl;
import org.danilskryl.restapi.exception.ResponseData;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(name = "ProductServlet", value = "/api/v1/products/*")
public class ProductServlet extends HttpServlet {
    private final ProductService service = new ProductServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");

        if (pathInfo == null || pathInfo.equals("/")) {
            List<ProductTo> allProducts = service.getAllProducts();
            String json = mapper.writeValueAsString(allProducts);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);

            try {
                long productId = Long.parseLong(idString);
                ProductTo product = service.getProductById(productId);

                if (product == null) {
                    resp.setStatus(SC_NOT_FOUND);
                    resp.getWriter().write(mapper.writeValueAsString(
                            ResponseData.constructResponseData(SC_NOT_FOUND, "Product not found with ID " + productId)
                    ));
                    return;
                }

                String json = mapper.writeValueAsString(product);
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
        ProductTo convertedProduct = mapper.readValue(readJson(req.getReader()), ProductTo.class);

        ProductTo productTo = service.saveProduct(convertedProduct);

        String json = mapper.writeValueAsString(productTo);
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

        ProductTo convertedProduct = mapper.readValue(readJson(req.getReader()), ProductTo.class);

        ProductTo productTo = service.updateProduct(convertedProduct);

        String json = mapper.writeValueAsString(productTo);
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
                long productId = Long.parseLong(idString);

                boolean b = service.removeProduct(productId);

                resp.setStatus(SC_OK);
                resp.getWriter().write(mapper.writeValueAsString(
                        ResponseData.constructResponseData(SC_OK, "Successfully removed=" + b))
                );
            } catch (NumberFormatException e) {
                resp.getWriter().write(mapper.writeValueAsString(
                        ResponseData.constructResponseData(SC_BAD_REQUEST, "Path must be contains only numbers")
                ));
                resp.setStatus(SC_BAD_REQUEST);
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