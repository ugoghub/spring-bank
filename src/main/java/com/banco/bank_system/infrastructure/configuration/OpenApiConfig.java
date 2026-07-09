package com.banco.bank_system.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankSystemOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Bank System API")
                                .description("""
                                        API REST para gerenciamento de clientes,
                                        contas bancárias e transações financeiras.
                                        Desenvolvida com Spring Boot utilizando
                                        Clean Architecture, DDD e testes automatizados.
                                        """)
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Hugo")
                                                .email("seu-email@email.com")
                                                .url("https://github.com/seuusuario")
                                )
                );
    }
}
