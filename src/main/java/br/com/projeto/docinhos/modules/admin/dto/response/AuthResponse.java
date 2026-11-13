package br.com.projeto.docinhos.modules.admin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
    @Schema(
            description = "Token JWT gerado após a autenticação",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
        String token) {}
