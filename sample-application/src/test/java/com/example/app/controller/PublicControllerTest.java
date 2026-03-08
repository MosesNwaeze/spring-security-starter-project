
package com.example.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PublicControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  void healthEndpointShouldReturn200() throws Exception {
    mvc.perform(get("/api/public/health"))
      .andExpect(status().isOk())
      .andExpect(content().string(containsString("UP")));
  }

}
