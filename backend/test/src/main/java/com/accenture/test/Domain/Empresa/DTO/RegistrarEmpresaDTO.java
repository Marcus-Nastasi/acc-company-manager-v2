package com.accenture.test.Domain.Empresa.DTO;

public record RegistrarEmpresaDTO(
        String cnpj,
        String nome_fantasia,
        String cep
) {}
