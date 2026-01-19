package com.deukyunlee.indexer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 27.
 */
@Configuration
public class ApiDocConfig {
    @Value("${swagger.schema.url}")
    private String url;
    @Value("${swagger.schema.description}")
    private String description;

    @Bean
    public OpenAPI openApiConfig() {
        Contact c = new Contact();
        c.setName("Ethereum Indexer");
        c.setUrl("http://localhost:8080/");

        Server svr = new Server()
                .url(url)
                .description(description);

        Info i = new Info()
                .title("Ethereum Indexer Documentation")
                .version("1.0.0")
                .contact(c)
                .description("Documentation of Ethereum Indexer");

        return new OpenAPI().info(i).addServersItem(svr);
    }
}
