package br.com.projeto.docinhos.config;

import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.imagem")
public record ImagemProperties(String caminho, Set<String> tiposPermitidos) {}
