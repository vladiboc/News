package org.example.news.web.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.List;
import org.example.news.db.entity.Role;
import org.example.news.db.entity.RoleType;
import org.example.news.db.entity.User;
import org.example.news.db.repository.UserRepository;
import org.example.news.mapper.v1.UserMapper;
import org.example.news.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.nio.charset.StandardCharsets;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Testcontainers
public abstract class AbstractControllerTest {
  protected static PostgreSQLContainer postgreSQLContainer;

  static {
    DockerImageName postgres = DockerImageName.parse("postgres:12.3");
    postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres).withReuse(true);
    postgreSQLContainer.start();
  }

  @DynamicPropertySource
  public static void registerProperties(DynamicPropertyRegistry registry) {
    String jdbcUrl = postgreSQLContainer.getJdbcUrl();

    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.url", () -> jdbcUrl);
  }

  @Autowired
  protected UserMapper userMapper;
  @Autowired
  protected UserService userService;
  @Autowired
  protected UserRepository userRepository;
  @Autowired
  protected WebApplicationContext context;
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {

    this.userService.createNewUser(new User(
        "testUser",
        "testUserPassword",
        List.of(Role.from(RoleType.ROLE_USER))
    ));

    this.userService.createNewUser(new User(
        "testAdmin",
        "testAdminPassword",
        List.of(Role.from(RoleType.ROLE_ADMIN))
    ));
  }

  @AfterEach
  public void clear() {
    this.userRepository.deleteAll();
  }

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
