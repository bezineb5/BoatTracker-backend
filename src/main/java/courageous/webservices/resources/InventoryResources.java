package courageous.webservices.resources;

import java.util.List;

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
}