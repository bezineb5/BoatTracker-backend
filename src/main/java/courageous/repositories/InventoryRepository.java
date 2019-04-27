package courageous.repositories;

import courageous.models.InventoryItem;

public class InventoryRepository extends BaseRepository<InventoryItem> {
    public InventoryRepository() {
        super(InventoryItem.class);
    }
}