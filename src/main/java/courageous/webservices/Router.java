package courageous.webservices;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;

import javax.ws.rs.NotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import courageous.models.Assignation;
import courageous.models.InventoryItem;
import courageous.webservices.exceptions.BaseException;
import courageous.webservices.resources.AssignationResources;
import courageous.webservices.resources.InventoryResources;
import courageous.webservices.resources.LocationResources;
import courageous.webservices.messages.AssignationUpdate;
import courageous.webservices.messages.LocationUpdate;

public class Router {
    private static final Logger log = LogManager.getLogger(Router.class);

    public void registerResources() {
        final JsonTransformer transformer = new JsonTransformer();
        final ObjectMapper objectMapper = new ObjectMapper();

        enableCORS("*", "POST, GET, OPTIONS, DELETE", "Content-Type");

        exception(BaseException.class, (exception, request, response) -> {
            // Handle the exception here
            response.status(exception.getHttpCode());
            response.body(exception.getMessage());
        });

        exception(Exception.class, (exception, request, response) -> {
            // Handle the exception here
            log.error("Unexpected exception", exception);
            response.status(500);
            response.body(exception.getMessage());
        });

        // Register our resources
        registerInventoryResources(transformer, objectMapper);
        registerAssignationResources(transformer, objectMapper);
        registerLocationResources(transformer, objectMapper);
        registerUserResources(transformer, objectMapper);
    }

    /**
     * From http://sparkjava.com/tutorials/cors
     * 
     * @param origin
     * @param methods
     * @param headers
     */
    private void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);

            response.type("application/json");
        });
    }

    private void registerInventoryResources(JsonTransformer transformer, ObjectMapper objectMapper) {
        get("/items", (request, response) -> {
            log.info("Listing inventory");
            return InventoryResources.list();
        }, transformer);

        post("/items", (request, response) -> {
            InventoryItem newItem = objectMapper.readValue(request.body(), InventoryItem.class);
            return InventoryResources.create(newItem);
        }, transformer);

        get("/items/:id", (request, response) -> {
            String itemId = request.params(":id");
            return InventoryResources.get(itemId);
        }, transformer);

        delete("/items/:id", (request, response) -> {
            String itemId = request.params(":id");
            InventoryResources.delete(itemId);
            return true;
        }, transformer);
    }

    private void registerAssignationResources(JsonTransformer transformer, ObjectMapper objectMapper) {
        get("/assignations", (request, response) -> {
            return AssignationResources.list();
        }, transformer);

        post("/assignations", (request, response) -> {
            Assignation newItem = objectMapper.readValue(request.body(), Assignation.class);
            return AssignationResources.create(newItem);
        }, transformer);

        get("/assignations/:id", (request, response) -> {
            String itemId = request.params(":id");
            return AssignationResources.get(itemId);
        }, transformer);

        delete("/assignations/:id", (request, response) -> {
            String itemId = request.params(":id");
            AssignationResources.delete(itemId);
            return true;
        }, transformer);
    }

    private void registerLocationResources(JsonTransformer transformer, ObjectMapper objectMapper) {
        get("/locations", (request, response) -> {
            return LocationResources.list();
        }, transformer);

        get("/locations/:id", (request, response) -> {
            String itemId = request.params(":id");
            return LocationResources.get(itemId);
        }, transformer);

        delete("/locations/:id", (request, response) -> {
            String itemId = request.params(":id");
            LocationResources.delete(itemId);
            return true;
        }, transformer);
    }

    /**
     * We use a different URL, for access control purposes
     * 
     * @param transformer
     * @param objectMapper
     */
    private void registerUserResources(JsonTransformer transformer, ObjectMapper objectMapper) {
        // Check-out
        post("/users/:secret/assignation", (request, response) -> {
            String secret = request.params(":secret");
            AssignationUpdate newItem = objectMapper.readValue(request.body(), AssignationUpdate.class);
            return AssignationResources.checkOut(secret, newItem);
        }, transformer);

        // Check-in
        delete("/users/:secret/assignation", (request, response) -> {
            String secret = request.params(":secret");
            AssignationResources.checkIn(secret);
            return true;
        }, transformer);

        // Retrieve associated inventory item
        get("/users/:secret/item", (request, response) -> {
            String secret = request.params(":secret");
            Assignation assignation = AssignationResources.getFromSecret(secret);
            if (assignation != null) {
                return InventoryResources.get(assignation.getInventoryItemId().toString());
            } else {
                throw new NotFoundException("No item found for that secret");
            }
        }, transformer);

        // Track location
        post("/users/:secret/location", (request, response) -> {
            String secret = request.params(":secret");
            LocationUpdate newLocation = objectMapper.readValue(request.body(), LocationUpdate.class);
            return LocationResources.create(secret, newLocation);
        }, transformer);
    }
}