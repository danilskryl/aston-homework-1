package org.danilskryl.restapi.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.danilskryl.restapi.dto.MarketTo;
import org.danilskryl.restapi.exception.ResponseData;
import org.danilskryl.restapi.service.MarketService;
import org.danilskryl.restapi.service.impl.MarketServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(name = "MarketServlet", value = "/api/v1/markets/*")
public class MarketServlet extends HttpServlet {
    private final MarketService service = new MarketServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");

        if (!isPathNotNull(req.getPathInfo())) {
            List<MarketTo> allMarkets = service.getAllMarkets();
            String json = mapper.writeValueAsString(allMarkets);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } else {
            String idString = pathInfo.substring(1);

            try {
                long marketId = Long.parseLong(idString);
                MarketTo market = service.getMarketById(marketId);

                if (market == null) {
                    resp.setStatus(SC_NOT_FOUND);
                    resp.getWriter().write(mapper.writeValueAsString(
                            ResponseData.constructResponseData(SC_NOT_FOUND, "Market not found with ID " + marketId)
                    ));
                    return;
                }

                String json = mapper.writeValueAsString(market);
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
        if (isPathNotNull(req.getPathInfo())) {
            resp.getWriter().write(mapper.writeValueAsString(
                    ResponseData.constructResponseData(
                            SC_BAD_REQUEST, "Invalid path"
                    )
            ));
            resp.setStatus(SC_BAD_REQUEST);
            return;
        }
        MarketTo convertedMarket = mapper.readValue(readJson(req.getReader()), MarketTo.class);

        MarketTo marketTo = service.saveMarket(convertedMarket);

        String json = mapper.writeValueAsString(marketTo);
        resp.setContentType("application/json");
        resp.setStatus(SC_CREATED);
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            String idString = pathInfo.substring(1);

            try {
                long marketId = Long.parseLong(idString);
                boolean b = service.removeMarket(marketId);

                resp.setContentType("application/json");
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isPathNotNull(req.getPathInfo())) {
            resp.getWriter().write(mapper.writeValueAsString(
                    ResponseData.constructResponseData(SC_BAD_REQUEST, "Invalid path")
            ));
            resp.setStatus(SC_BAD_REQUEST);
            return;
        }
        MarketTo convertedMarket = mapper.readValue(readJson(req.getReader()), MarketTo.class);

        MarketTo marketTo = service.updateMarket(convertedMarket);

        String json = mapper.writeValueAsString(marketTo);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(json);
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