package courageous.webservices.resources;

import java.util.List;

import courageous.models.Assignation;
import courageous.models.Location;
import courageous.repositories.LocationRepository;
import courageous.webservices.exceptions.InvalidRequestException;
import courageous.webservices.exceptions.NotAuthorizedException;
import courageous.webservices.messages.LocationUpdate;

public class LocationResources {

    private LocationResources() {
    }

    public static List<Location> list() {
        return new LocationRepository().list();
    }

    public static Location get(String inventoryItemId) throws InvalidRequestException {
        Location location = new Location().inventoryItemId(ResourcesHelper.parseUuid(inventoryItemId));
        return new LocationRepository().get(location);
    }

    public static void delete(String inventoryItemId) throws InvalidRequestException {
        Location deleteLocation = new Location()
                .inventoryItemId(ResourcesHelper.parseUuid(inventoryItemId));

        new LocationRepository().delete(deleteLocation);
    }

    public static Location create(String secret, LocationUpdate locationUpdate)
            throws InvalidRequestException, NotAuthorizedException {

                // Get the inventory item from the secret
        final Assignation assignation = AssignationResources.getFromSecret(secret);

        if (assignation == null) {
            throw new NotAuthorizedException("This item is not assigned to you");
        }

        Location newLocation = new Location()
                .inventoryItemId(assignation.getInventoryItemId())
                .latitude(locationUpdate.getLatitude())
                .longitude(locationUpdate.getLongitude());

        return new LocationRepository().save(newLocation);
    }
}