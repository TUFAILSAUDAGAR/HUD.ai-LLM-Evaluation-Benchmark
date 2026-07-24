package com.northstar.commerce.inventory;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
  private final String defaultWarehouse;
  public InventoryService(@Value("${inventory.default-warehouse}") String defaultWarehouse) {
    this.defaultWarehouse = defaultWarehouse;
  }

  public InventoryController.InventorySnapshot lookup(String sku, String warehouse) {
    String selectedWarehouse = warehouse == null || warehouse.isBlank() ? defaultWarehouse : warehouse;
    return new InventoryController.InventorySnapshot(sku, selectedWarehouse, 42, Instant.now());
  }
}
