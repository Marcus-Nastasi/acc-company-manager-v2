package com.accenture.test.adapter.output.company;

import java.io.Serializable;
import java.util.UUID;

public record CompanyCleanDto(
        UUID id,
        String cnpj,
        String nome_fantasia,
        String cep
) implements Serializable {}
