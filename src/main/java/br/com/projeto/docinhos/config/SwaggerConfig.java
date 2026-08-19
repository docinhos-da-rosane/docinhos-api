package br.com.projeto.docinhos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info =
  @Info(
    title = "Docinhos API",
    version = "1.0",
    summary = "API para consulta e gerenciamento do catálogo de docinhos."))
public class SwaggerConfig {}
