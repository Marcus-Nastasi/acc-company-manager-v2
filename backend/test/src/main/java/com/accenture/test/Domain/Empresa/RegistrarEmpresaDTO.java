package com.accenture.test.Domain.Empresa;

public record RegistrarEmpresaDTO(
        String cnpj,
        String nome_fantasia,
        String cep
) {}
