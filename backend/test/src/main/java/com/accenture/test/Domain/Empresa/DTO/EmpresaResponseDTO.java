package com.accenture.test.Domain.Empresa.DTO;

import java.util.UUID;

public record EmpresaResponseDTO(
       UUID id,
       String cnpj,
       String nome_fantasia,
       String cep
) {}
