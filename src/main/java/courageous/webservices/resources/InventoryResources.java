package courageous.webservices.resources;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;

import courageous.models.InventoryItem;
import courageous.repositories.InventoryRepository;
import courageous.webservices.exceptions.InvalidRequestException;

public class InventoryResources {
    private InventoryResources() {
    }

    public static List<InventoryItem> list() {
        return new InventoryRepository().list();
    }

    public static InventoryItem create(InventoryItem newItem) throws InvalidRequestException {
        if (ResourcesHelper.isEmpty(newItem.getName())) {
            throw new InvalidRequestException("Name cannot be empty");
        }

        InventoryItem dbItem = new InventoryRepository().save(newItem);
        return dbItem;
    }

    public static void delete(String itemId) throws InvalidRequestException {
        InventoryItem deleteItem = new InventoryItem().id(ResourcesHelper.parseUuid(itemId));
        new InventoryRepository().delete(deleteItem);
    }

    public static InventoryItem get(String itemId) throws InvalidRequestException {
        InventoryItem retrieveItem = new InventoryItem().id(ResourcesHelper.parseUuid(itemId));
        return new InventoryRepository().get(retrieveItem);
    }

    public static InventoryItem getFromDeviceID(String deviceId) throws InvalidRequestException {
        if (deviceId == null) {
            throw new InvalidRequestException("Device ID cannot be empty");
        }

        InventoryItem searchItem = new InventoryItem().deviceId(deviceId);
        InventoryRepository repo = new InventoryRepository();

        final DynamoDBQueryExpression<InventoryItem> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression.setHashKeyValues(searchItem);
        queryExpression.setIndexName("DeviceId-index");
        queryExpression.setConsistentRead(false); // cannot use consistent read on GSI
        final PaginatedQueryList<InventoryItem> results = repo.getMapper().query(InventoryItem.class, queryExpression);

        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

}