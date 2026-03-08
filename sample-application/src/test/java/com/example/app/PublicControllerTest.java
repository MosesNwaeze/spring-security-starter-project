
package com.example.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PublicControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void healthEndpointShouldReturn200() throws Exception {

        mvc.perform(get("/api/public/health"))
                .andExpect(status().isOk())
            .andExpect((ResultMatcher) content().string(containsString("UP")));
    }

}
