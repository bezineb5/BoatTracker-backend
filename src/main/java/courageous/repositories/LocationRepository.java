package courageous.repositories;

import java.util.UUID;

import courageous.models.Location;

public class LocationRepository extends BaseRepository<Location> {
    public LocationRepository() {
        super(Location.class);
    }

    public Location findItemLocation(UUID inventoryItemId) {
        return getMapper().load(Location.class, inventoryItemId);
    }
}