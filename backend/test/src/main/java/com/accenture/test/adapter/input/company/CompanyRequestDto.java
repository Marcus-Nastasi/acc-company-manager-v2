package com.accenture.test.adapter.input.company;

import com.accenture.test.domain.supplier.Supplier;

import java.util.List;
import java.util.UUID;

public record CompanyRequestDto(
        UUID id,
        String cnpj,
        String nome_fantasia,
        String cep,
        List<Supplier> fornecedores
) {}
