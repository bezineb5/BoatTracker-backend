package courageous.webservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private static final Logger log = LogManager.getLogger(JsonTransformer.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String render(Object model) {
        try {
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            log.error("Cannot serialize object", e);
            return null;
        }
    }

}