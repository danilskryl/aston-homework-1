package org.danilskryl.restapi.servlets.data.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestUtil {
    public static String removeFieldFromJson(String json, String fieldName) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        ((ObjectNode) jsonNode).remove(fieldName);

        return objectMapper.writeValueAsString(jsonNode);
    }
}
