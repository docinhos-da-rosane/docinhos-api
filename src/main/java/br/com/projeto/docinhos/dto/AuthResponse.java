package br.com.projeto.docinhos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
    @Schema(
            description = "Token JWT gerado após a autenticação",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
        String token) {}
