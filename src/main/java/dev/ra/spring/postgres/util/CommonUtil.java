package dev.ra.spring.postgres.util;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CommonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
            .lookupClass());

    private CommonUtil() {
    }

    /*
     * Method is used to convert Java object to JSON String
     */
    public static String convertObjectToJsonString(Object requestObject) {
        String jsonString = null;
        try {
            if (requestObject != null) {
                jsonString = objectMapper.writeValueAsString(requestObject);
            }
        } catch (JsonProcessingException exception) {
            jsonString = requestObject.toString();
            LOG.error("Error occurred while converting request object : {} to json string", requestObject.toString());
        }
        return jsonString;
    }

}
