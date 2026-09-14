package br.com.projeto.docinhos.integration.image.cloudinary;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cloudinary")
public record CloudinaryProperties(String cloudName, String apiKey, String apiSecret) {}
