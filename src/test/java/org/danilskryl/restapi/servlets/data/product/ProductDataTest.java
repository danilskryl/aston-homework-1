package org.danilskryl.restapi.servlets.data.product;

public class ProductDataTest {
    public static final String[] ARRAY_PRODUCT_JSON = {
            "{\"id\":1,\"name\":\"Browser\",\"description\":\"Google browser\",\"marketId\":3}",
            "{\"id\":2,\"name\":\"YouTube\",\"description\":\"Video hosting\",\"marketId\":3}",
            "{\"id\":3,\"name\":\"Gmail\",\"description\":\"Google mail\",\"marketId\":3}",
            "{\"id\":4,\"name\":\"Browser\",\"description\":\"Yandex browser\",\"marketId\":2}",
            "{\"id\":5,\"name\":\"Food\",\"description\":\"Yandex food\",\"marketId\":2}",
            "{\"id\":6,\"name\":\"Maps\",\"description\":\"Yandex maps\",\"marketId\":2}",
            "{\"id\":7,\"name\":\"Taxi\",\"description\":\"Yandex taxi\",\"marketId\":2}",
            "{\"id\":8,\"name\":\"Facebook\",\"description\":\"Social network\",\"marketId\":1}",
            "{\"id\":9,\"name\":\"Instagram\",\"description\":\"Social network\",\"marketId\":1}",
            "{\"id\":10,\"name\":\"Threads\",\"description\":\"Social network\",\"marketId\":1}"
    };

    public static final String ALL_PRODUCTS_JSON =
            "[{\"id\":1,\"name\":\"Browser\",\"description\":\"Google browser\",\"marketId\":3}," +
            "{\"id\":2,\"name\":\"YouTube\",\"description\":\"Video hosting\",\"marketId\":3}," +
            "{\"id\":3,\"name\":\"Gmail\",\"description\":\"Google mail\",\"marketId\":3}," +
            "{\"id\":4,\"name\":\"Browser\",\"description\":\"Yandex browser\",\"marketId\":2}," +
            "{\"id\":5,\"name\":\"Food\",\"description\":\"Yandex food\",\"marketId\":2}," +
            "{\"id\":6,\"name\":\"Maps\",\"description\":\"Yandex maps\",\"marketId\":2}," +
            "{\"id\":7,\"name\":\"Taxi\",\"description\":\"Yandex taxi\",\"marketId\":2}," +
            "{\"id\":8,\"name\":\"Facebook\",\"description\":\"Social network\",\"marketId\":1}," +
            "{\"id\":9,\"name\":\"Instagram\",\"description\":\"Social network\",\"marketId\":1}," +
            "{\"id\":10,\"name\":\"Threads\",\"description\":\"Social network\",\"marketId\":1}]";

    public static final String NEW_PRODUCT_JSON =
            "{\"id\":11,\"name\":\"TestProduct\",\"description\":\"TestDescription\",\"marketId\":1}";

    public static final String UPDATED_PRODUCT_JSON =
            "{\"id\":11,\"name\":\"UpdatedTestProduct\",\"description\":\"UpdatedTestDescription\",\"marketId\":2}";

    public static final String SUCCESSFUL_DELETE_PRODUCT_JSON =
            "{\"status\":200,\"message\":\"Successfully removed=true\"}";
}
