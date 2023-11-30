package org.danilskryl.restapi.servlets.data.order;

public class OrderDataTest {
    public static final String[] ARRAY_ORDER_JSON = {
            "{\"id\":1,\"orderDate\":\"2023-12-01 15:30:00\"}",
            "{\"id\":2,\"orderDate\":\"2023-12-15 16:45:00\"}",
            "{\"id\":3,\"orderDate\":\"2024-01-05 12:00:00\"}",
            "{\"id\":4,\"orderDate\":\"2024-02-10 14:20:00\"}",
            "{\"id\":5,\"orderDate\":\"2024-03-20 18:30:00\"}",
            "{\"id\":6,\"orderDate\":\"2024-04-02 10:45:00\"}",
            "{\"id\":7,\"orderDate\":\"2024-05-12 11:15:00\"}",
            "{\"id\":8,\"orderDate\":\"2024-06-25 17:00:00\"}",
            "{\"id\":9,\"orderDate\":\"2024-07-08 09:30:00\"}",
            "{\"id\":10,\"orderDate\":\"2024-08-19 13:55:00\"}"
    };

    public static final String ALL_ORDERS_JSON =
                    "[{\"id\":1,\"orderDate\":\"2023-12-01 15:30:00\"}," +
                    "{\"id\":2,\"orderDate\":\"2023-12-15 16:45:00\"}," +
                    "{\"id\":3,\"orderDate\":\"2024-01-05 12:00:00\"}," +
                    "{\"id\":4,\"orderDate\":\"2024-02-10 14:20:00\"}," +
                    "{\"id\":5,\"orderDate\":\"2024-03-20 18:30:00\"}," +
                    "{\"id\":6,\"orderDate\":\"2024-04-02 10:45:00\"}," +
                    "{\"id\":7,\"orderDate\":\"2024-05-12 11:15:00\"}," +
                    "{\"id\":8,\"orderDate\":\"2024-06-25 17:00:00\"}," +
                    "{\"id\":9,\"orderDate\":\"2024-07-08 09:30:00\"}," +
                    "{\"id\":10,\"orderDate\":\"2024-08-19 13:55:00\"}]";

    public static final String UPDATED_ORDER_JSON = "{\"id\":11,\"orderDate\":\"2015-12-12 13:00:00\"}";

    public static final String SUCCESSFUL_DELETE_ORDER_JSON =
            "{\"status\":200,\"message\":\"Successfully removed=true\"}";

    public static final String ORDER_DATE = "2015-12-12T13:00:00";
}
