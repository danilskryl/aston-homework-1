package org.danilskryl.restapi.servlets.data.market;

public class MarketDataTest {
    public static final String[] ARRAY_MARKET_JSON = {"{\"id\":1,\"name\":\"Meta\"}",
            "{\"id\":2,\"name\":\"Yandex\"}",
            "{\"id\":3,\"name\":\"Google\"}"};

    public static final String ALL_MARKETS_JSON = "[{\"id\":1,\"name\":\"Meta\"}," +
            "{\"id\":2,\"name\":\"Yandex\"}," +
            "{\"id\":3,\"name\":\"Google\"}]";

    public static final String NEW_MARKET_JSON = "{\"id\":4,\"name\":\"TestMarket\"}";

    public static final String UPDATED_MARKET_JSON = "{\"id\":4,\"name\":\"UpdatedTestMarket\"}";

    public static final String SUCCESSFUL_DELETE_MARKET_JSON =
            "{\"status\":200,\"message\":\"Successfully removed=true\"}";
}
