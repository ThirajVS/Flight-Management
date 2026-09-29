package com.aerocadet.portal.system;

import com.aerocadet.portal.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SystemStatusController.class)
@Import(SecurityConfig.class)
class SystemStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsPublicServiceStatus() throws Exception {
        mockMvc.perform(get("/api/public/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application").value("aerocadet-api"))
                .andExpect(jsonPath("$.service").value("AeroCadet API"))
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void protectsUnknownApiRoutes() throws Exception {
        mockMvc.perform(get("/api/private/example"))
                .andExpect(status().isForbidden());
    }
}

