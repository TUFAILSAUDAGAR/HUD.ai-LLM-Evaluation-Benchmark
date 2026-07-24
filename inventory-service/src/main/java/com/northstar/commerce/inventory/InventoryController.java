package com.northstar.commerce.inventory;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class InventoryController {
  private static final Logger LOG = LoggerFactory.getLogger(InventoryController.class);
  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) { this.inventoryService = inventoryService; }

  @GetMapping("/health")
  public ResponseEntity<Map<String, String>> health() { return ResponseEntity.ok(Map.of("status", "UP")); }

  @GetMapping("/inventory")
  public InventorySnapshot inventory(@RequestParam @NotBlank String sku, @RequestParam(required = false) String warehouse) {
    LOG.info("Inventory lookup requested for sku={} warehouse={}", sku, warehouse);
    return inventoryService.lookup(sku, warehouse);
  }

  public record InventorySnapshot(String sku, String warehouse, int availableQuantity, Instant observedAt) {}
}
