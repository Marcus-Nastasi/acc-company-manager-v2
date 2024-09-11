package com.accenture.test.Domain.Empresa;

import jakarta.persistence.Column;

public record RegistrarEmpresaDTO(
        String cnpj,
        String nome_fantasia,
        String cep
) {}
