package dev.ra.spring.postgres.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

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
            log.error("Error occurred while converting request object : {} to json string", requestObject.toString());
        }
        return jsonString;
    }
    
	/*
	 * Method is used to convert JSON String to java class object
	 */
	public static <T> T convertJsonStringToObject(String jsonAsString, Class<T> valueType) {
		try {
			String unwrappedJSON = objectMapper.readValue(jsonAsString, String.class);
			return objectMapper.readValue(unwrappedJSON, valueType);
		} catch (Exception exception) {
			log.error("Unable to convert JSON String to Java Object : {}", exception.getMessage(), exception);
			throw new RuntimeException("Unable to convert JSON String to Java Object");
		}
	}

}
