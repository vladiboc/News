package org.example.news.web.controller.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;

  protected String mockGet(String url, HttpStatus expectedStatus) throws Exception {
    return this.mockMvc.perform(get(url))
        .andExpect(status().is(expectedStatus.value()))
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  protected String mockPost(String url, Object request, HttpStatus expectedStatus) throws Exception {
    return this.mockMethod(post(url), request, expectedStatus);
  }

  protected String mockPut(String url, Object request, HttpStatus expectedStatus) throws Exception {
    return this.mockMethod(put(url), request, expectedStatus);
  }

  protected void mockDelete(String url, HttpStatus expectedStatus) throws Exception {
    this.mockMvc.perform(delete(url))
        .andExpect(status().is(expectedStatus.value()));
  }

  private String mockMethod(MockHttpServletRequestBuilder builder, Object request, HttpStatus expectedStatus) throws Exception {
    return this.mockMvc.perform(builder
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().is(expectedStatus.value()))
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }
}
