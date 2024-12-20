package com.accenture.test.adapter.output.empresa;

import java.io.Serializable;
import java.util.UUID;

public record EmpresaCleanDto(
        UUID id,
        String cnpj,
        String nome_fantasia,
        String cep
) implements Serializable {}
