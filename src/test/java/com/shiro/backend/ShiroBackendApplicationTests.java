package com.shiro.backend;

import com.shiro.backend.config.CorsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ShiroBackendApplicationTests {

    @Autowired
    CorsProperties corsProperties;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void outputCorsTest() {
        System.out.println("Mission Start");
        for (int alice = 0; alice < corsProperties.getAllowedOrigins().size(); alice++) {
            System.out.println("爱丽丝：" + corsProperties.getAllowedOrigins().get(alice));
            System.out.println("爱丽丝的长度：" + corsProperties.getAllowedOrigins().get(alice).length());
        }
    }


    @Test
    void testCorsAllowedOrigins() throws Exception {
        String origin = corsProperties.getAllowedOrigins().get(0);

        mockMvc.perform(options("/api/any-endpoint")
                        .header("Origin", origin)
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", origin));
    }

    @Test
    void testCorsAllowedMethods() throws Exception {
        String origin = corsProperties.getAllowedOrigins().get(0);

        mockMvc.perform(options("/api/any-endpoint")
                        .header("Origin", origin)
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Methods", String.join(",", corsProperties.getAllowedMethods())));
    }

    @Test
    void testCorsAllowedHeaders() throws Exception {
        String origin = corsProperties.getAllowedOrigins().get(0);

        mockMvc.perform(options("/api/any-endpoint")
                        .header("Origin", origin)
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Headers", String.join(",", "Content-Type")));
    }

    @Test
    void testCorsAllowCredentials() throws Exception {
        String origin = corsProperties.getAllowedOrigins().get(0);

        mockMvc.perform(options("/api/any-endpoint")
                        .header("Origin", origin)
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Credentials", corsProperties.isAllowCredentials() ? "true" : "false"));
    }
}
