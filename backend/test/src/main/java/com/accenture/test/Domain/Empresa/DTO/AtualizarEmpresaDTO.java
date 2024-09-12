package com.accenture.test.Domain.Empresa.DTO;

public record AtualizarEmpresaDTO(
        String cnpj,
        String nome_fantasia,
        String cep
) {}
