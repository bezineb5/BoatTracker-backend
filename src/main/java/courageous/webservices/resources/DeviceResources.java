package courageous.webservices.resources;

import courageous.devices.SigfoxOyster;
import courageous.models.InventoryItem;
import courageous.models.Location;
import courageous.repositories.LocationRepository;
import courageous.webservices.exceptions.InvalidRequestException;
import courageous.webservices.exceptions.NotFoundException;
import courageous.webservices.messages.SigfoxMessage;

public class DeviceResources {
    private DeviceResources() {
    }

    public static Location create(SigfoxMessage deviceMessage) throws InvalidRequestException, NotFoundException {

        if (deviceMessage == null || deviceMessage.getData() == null || deviceMessage.getDevice() == null) {
            throw new InvalidRequestException("Incomplete input data");
        }

        // Parse the location payload
        final SigfoxOyster.Location deviceLocation = SigfoxOyster.parse(deviceMessage.getData());
        if (deviceLocation == null) {
            throw new InvalidRequestException("Unable to parse input data");
        }

        // Get the inventory item from the device ID
        final InventoryItem item = InventoryResources.getFromDeviceID(deviceMessage.getDevice());

        if (item == null) {
            throw new NotFoundException("This device ID is not assigned to any item");
        }

        Location newLocation = new Location().inventoryItemId(item.getId()).latitude(deviceLocation.getLatitude())
                .longitude(deviceLocation.getLongitude());

        return new LocationRepository().save(newLocation);
    }
}