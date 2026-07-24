package com.northstar.payments.settlement.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SettlementControllerTest {
    @Autowired MockMvc mvc;
    @Test void acceptsValidSettlement() throws Exception {
        mvc.perform(post("/v1/settlements").contentType(MediaType.APPLICATION_JSON)
                .content("{\"merchantId\":\"mrc_1029\",\"amountMinor\":1250,\"currency\":\"USD\"}"))
           .andExpect(status().isAccepted()).andExpect(jsonPath("$.status").value("ACCEPTED"));
    }
}
