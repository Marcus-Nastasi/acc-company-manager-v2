package com.accenture.test.adapter.output.empresa;

import java.util.UUID;

public record EmpresaResponseDTO(
       UUID id,
       String cnpj,
       String nome_fantasia,
       String cep
) {}
