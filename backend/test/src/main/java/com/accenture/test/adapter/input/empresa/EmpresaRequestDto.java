package com.accenture.test.adapter.input.empresa;

import com.accenture.test.domain.fornecedor.Fornecedor;

import java.util.List;
import java.util.UUID;

public record EmpresaRequestDto(
        UUID id,
        String cnpj,
        String nome_fantasia,
        String cep,
        List<Fornecedor> fornecedores
) {}
