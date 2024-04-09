package org.example.news.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
  @Value("${server.port:8080}")
  private int applicationPort;

  @Bean
  public OpenAPI openApiDescription() {
    Server localhostServer = new Server();
    localhostServer.setUrl("http://localhost:" + applicationPort);
    localhostServer.setDescription("Локальная машина");

    Server productionServer = new Server();
    productionServer.setUrl("http://10.10.10.10:" + applicationPort);
    productionServer.setDescription("Продуктовая машина");

    Contact contact = new Contact();
    contact.setName("Vladislav Bochkarev");
    contact.setEmail("vladiboc@gmail.com");
    contact.setUrl("https://https://github.com/vladiboc");

    License gplLicense = new License().name("GNU GPL v3").url("https://www.gnu.org/licenses/gpl-3.0.ru.html");

    Info apiInfo = new Info()
        .title("Сервис новостей, бэкенд")
        .version("1.0")
        .contact(contact)
        .description("API бэкенда сервиса новостей")
        .termsOfService("https://https://github.com/vladiboc")
        .license(gplLicense);

    return new OpenAPI().info(apiInfo).servers(List.of(localhostServer, productionServer));
  }
}
