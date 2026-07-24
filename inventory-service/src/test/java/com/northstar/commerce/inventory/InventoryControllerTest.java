package com.northstar.commerce.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerTest {
  @Autowired private MockMvc mvc;
  @Test
  void returnsInventorySnapshot() throws Exception {
    mvc.perform(get("/inventory").param("sku", "SKU-1001"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sku").value("SKU-1001"));
  }

  @Test
  void returnsHealth() throws Exception {
    mvc.perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP"));
  }
}
